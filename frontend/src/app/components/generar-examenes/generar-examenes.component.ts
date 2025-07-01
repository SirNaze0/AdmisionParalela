import { Component, OnInit } from '@angular/core';
import { GeneradorService } from '../../services/generador.service';
import { BancoPreguntaService } from '../../services/banco-pregunta.service';

// Font Awesome Icons
import { faFileAlt, faChartBar, faBook, faCog, faClipboardList, faDownload, faTrash, faSchool } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-generar-examenes',
  templateUrl: './generar-examenes.component.html',
  styleUrls: ['./generar-examenes.component.css']
})
export class GenerarExamenesComponent implements OnInit {
  
  // Font Awesome Icons
  faFileAlt = faFileAlt;
  faChartBar = faChartBar;
  faBook = faBook;
  faCog = faCog;
  faClipboardList = faClipboardList;
  faDownload = faDownload;
  faTrash = faTrash;
  faSchool = faSchool;

  cantidadExamenes = 1;
  totalPreguntas = 0;
  cargando = false;
  resultado: any = null;

  constructor(
    private readonly generadorService: GeneradorService,
    private readonly bancoPreguntaService: BancoPreguntaService
  ) { }

  ngOnInit(): void {
    this.cargarTotalPreguntas();
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
      },
      error: (error: any) => {
        console.error('Error al generar exámenes:', error);
        this.cargando = false;
        alert('Error al generar exámenes: ' + (error.error?.mensaje || error.message));
      }
    });
  }

  limpiarExamenes(): void {
    if (confirm('¿Está seguro de que desea limpiar todos los exámenes generados?')) {
      this.generadorService.limpiarExamenes().subscribe({
        next: () => {
          alert('Exámenes limpiados exitosamente');
          this.resultado = null;
        },
        error: (error: any) => {
          console.error('Error al limpiar exámenes:', error);
          alert('Error al limpiar exámenes');
        }
      });
    }
  }
}