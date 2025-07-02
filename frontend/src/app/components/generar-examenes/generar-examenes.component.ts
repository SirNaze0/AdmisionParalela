import { Component, OnInit, OnDestroy } from '@angular/core';
import { GeneradorService, ExamenGeneradoDTO } from '../../services/generador.service';
import { BancoPreguntaService } from '../../services/banco-pregunta.service';
import { EstadoGlobalService, EstadoExamenes } from '../../services/estado-global.service';
import { Subscription } from 'rxjs';

// Font Awesome Icons
import { faFileAlt, faChartBar, faBook, faCog, faClipboardList, faDownload, faTrash, faSchool, faClipboardCheck, faTable, faUser, faIdCard, faFileCsv } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-generar-examenes',
  templateUrl: './generar-examenes.component.html',
  styleUrls: ['./generar-examenes.component.css']
})
export class GenerarExamenesComponent implements OnInit, OnDestroy {
  
  // Font Awesome Icons
  faFileAlt = faFileAlt;
  faChartBar = faChartBar;
  faBook = faBook;
  faCog = faCog;
  faClipboardList = faClipboardList;
  faDownload = faDownload;
  faTrash = faTrash;
  faSchool = faSchool;
  faClipboardCheck = faClipboardCheck;
  faTable = faTable;
  faUser = faUser;
  faIdCard = faIdCard;
  faFileCsv = faFileCsv;

  cantidadExamenes = 1;
  totalPreguntas = 0;
  cargando = false;
  resultado: any = null;
  examenesDetalle: ExamenGeneradoDTO[] = [];
  
  private estadoSubscription?: Subscription;

  constructor(
    private readonly generadorService: GeneradorService,
    private readonly bancoPreguntaService: BancoPreguntaService,
    private readonly estadoGlobalService: EstadoGlobalService
  ) { }

  ngOnInit(): void {
    this.cargarTotalPreguntas();
    this.suscribirseAlEstado();
    this.verificarEstadoExistente();
  }

  ngOnDestroy(): void {
    if (this.estadoSubscription) {
      this.estadoSubscription.unsubscribe();
    }
  }

  private suscribirseAlEstado(): void {
    this.estadoSubscription = this.estadoGlobalService.estadoExamenes$.subscribe(estado => {
      if (estado) {
        this.resultado = {
          nombreArchivo: estado.nombreArchivoZip,
          urlDescarga: estado.urlDescargaZip
        };
        this.examenesDetalle = estado.examenesDetalle;
      }
    });
  }

  private verificarEstadoExistente(): void {
    const estadoActual = this.estadoGlobalService.estadoExamenesActual;
    if (estadoActual) {
      this.resultado = {
        nombreArchivo: estadoActual.nombreArchivoZip,
        urlDescarga: estadoActual.urlDescargaZip
      };
      this.examenesDetalle = estadoActual.examenesDetalle;
    }
  }

  cargarTotalPreguntas(): void {
    this.bancoPreguntaService.obtenerPreguntas().subscribe({
      next: (preguntas) => {
        this.totalPreguntas = preguntas.length;
      },
      error: (error) => {
        console.error('Error al cargar total de preguntas:', error);
      }
    });
  }

  get puedeGenerar(): boolean {
    return this.cantidadExamenes > 0 && this.totalPreguntas > 0 && !this.cargando;
  }

  iniciarGeneracion(): void {
    if (!this.puedeGenerar) {
      return;
    }

    this.cargando = true;
    this.resultado = null;

    this.generadorService.generarExamenes(this.cantidadExamenes).subscribe({
      next: (response: any) => {
        this.resultado = response;
        this.cargando = false;
        console.log('Exámenes generados exitosamente:', response);
        
        // Cargar detalles de exámenes después de la generación
        this.cargarExamenesDetalle();
      },
      error: (error: any) => {
        console.error('Error al generar exámenes:', error);
        this.cargando = false;
        alert('Error al generar exámenes: ' + (error.error?.mensaje ?? error.message));
      }
    });
  }

  cargarExamenesDetalle(): void {
    this.generadorService.obtenerExamenesDetalle().subscribe({
      next: (examenes: ExamenGeneradoDTO[]) => {
        this.examenesDetalle = examenes;
        console.log('Detalles de exámenes cargados:', examenes);
        
        // Guardar estado en el servicio global
        if (this.resultado && examenes.length > 0) {
          const estadoExamenes: EstadoExamenes = {
            examenesGenerados: true,
            totalExamenes: examenes.length,
            nombreArchivoZip: this.resultado.nombreArchivo ?? 'Examenes.zip',
            urlDescargaZip: this.resultado.urlDescarga ?? '',
            examenesDetalle: examenes,
            fechaGeneracion: new Date()
          };
          
          this.estadoGlobalService.actualizarEstadoExamenes(estadoExamenes);
        }
      },
      error: (error: any) => {
        console.error('Error al cargar detalles de exámenes:', error);
      }
    });
  }

  limpiarExamenes(): void {
    if (confirm('¿Está seguro de que desea limpiar todos los exámenes generados?')) {
      this.generadorService.limpiarExamenes().subscribe({
        next: () => {
          alert('Exámenes limpiados exitosamente');
          this.resultado = null;
          this.examenesDetalle = [];
          
          // Limpiar estado global
          this.estadoGlobalService.limpiarEstadoExamenes();
        },
        error: (error: any) => {
          console.error('Error al limpiar exámenes:', error);
          alert('Error al limpiar exámenes');
        }
      });
    }
  }

  limpiarBaseDatosCompleta(): void {
    if (confirm('¿Está seguro de que desea limpiar TODA la base de datos? Esto eliminará postulantes, exámenes, resultados y banco de preguntas.')) {
      this.generadorService.limpiarBaseDatosCompleta().subscribe({
        next: () => {
          alert('Base de datos completamente limpiada');
          this.resultado = null;
          this.examenesDetalle = [];
          
          // Limpiar estado global
          this.estadoGlobalService.limpiarEstadoExamenes();
        },
        error: (error: any) => {
          console.error('Error al limpiar base de datos:', error);
          alert('Error al limpiar base de datos');
        }
      });
    }
  }

  descargarExamenIndividual(examen: ExamenGeneradoDTO): void {
    this.generadorService.descargarExamenIndividual(examen.examenId).subscribe({
      next: (blob: Blob) => {
        // Crear URL para el blob y descargar
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = examen.nombreArchivoPdf;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (error: any) => {
        console.error('Error al descargar examen:', error);
        alert('Error al descargar el examen: ' + (error.error?.mensaje ?? error.message));
      }
    });
  }

  generarPlantillaFichasOpticas(): void {
    if (this.examenesDetalle.length === 0) {
      alert('No hay exámenes generados. Primero genere los exámenes antes de crear la plantilla de fichas ópticas.');
      return;
    }

    // Crear contenido del CSV con los IDs reales
    let csvContent = 'codigoPostulante,idExamen,respuestas\n';
    
    this.examenesDetalle.forEach(examen => {
      // Generar respuestas de ejemplo (5 respuestas por defecto, el usuario las reemplazará)
      const respuestasEjemplo = 'ABCDE'; // 5 respuestas de ejemplo
      csvContent += `${examen.postulanteId},${examen.examenId},${respuestasEjemplo}\n`;
    });

    // Descargar el archivo
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'plantilla_fichas_opticas_examenes.csv';
    a.style.display = 'none';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);

    alert(`✅ Plantilla CSV generada exitosamente!\n\n📋 El archivo contiene ${this.examenesDetalle.length} registros con los IDs reales:\n\n• Postulantes: ${this.examenesDetalle.map(e => e.postulanteId).join(', ')}\n• Exámenes: ${this.examenesDetalle.map(e => e.examenId).join(', ')}\n\n📝 Edite las respuestas en el archivo descargado antes de usarlo en la corrección.`);
  }

  mostrarRelacionIds(): void {
    if (this.examenesDetalle.length === 0) {
      alert('No hay exámenes generados para mostrar la relación de IDs.');
      return;
    }

    let mensaje = '📊 RELACIÓN DE IDs PARA FICHAS ÓPTICAS:\n\n';
    mensaje += 'Postulante → Examen | Estudiante\n';
    mensaje += '────────────────────────────────\n';
    
    this.examenesDetalle.forEach((examen, index) => {
      mensaje += `${examen.postulanteId} → ${examen.examenId} | ${examen.nombresPostulante} ${examen.apellidosPostulante}\n`;
    });
    
    mensaje += '\n💡 Use estos IDs exactos en su archivo CSV de fichas ópticas.';
    
    alert(mensaje);
  }
}