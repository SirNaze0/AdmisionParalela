import { Component, OnInit } from '@angular/core';
import { BancoPreguntaService, BancoPreguntaDTO } from '../../services/banco-pregunta.service';
import { ModalService } from '../../services/modal.service';

// Font Awesome Icons
import { faQuestionCircle, faChartBar, faBook, faSearch, faPlus, faEdit, faTrash, faUpload, faTimes, faEye, faSchool, faFileAlt, faClipboardCheck, faTable } from '@fortawesome/free-solid-svg-icons';

interface FiltrosBusqueda {
  busqueda: string;
  curso: string;
}

@Component({
  selector: 'app-banco-preguntas',
  templateUrl: './banco-preguntas.component.html',
  styleUrls: ['./banco-preguntas.component.css']
})
export class BancoPreguntasComponent implements OnInit {

  // Font Awesome Icons
  faQuestionCircle = faQuestionCircle;
  faChartBar = faChartBar;
  faBook = faBook;
  faSearch = faSearch;
  faPlus = faPlus;
  faEdit = faEdit;
  faTrash = faTrash;
  faUpload = faUpload;
  faTimes = faTimes;
  faEye = faEye;
  faSchool = faSchool;
  faFileAlt = faFileAlt;
  faClipboardCheck = faClipboardCheck;
  faTable = faTable;

  preguntas: BancoPreguntaDTO[] = [];
  preguntasFiltradas: BancoPreguntaDTO[] = [];
  
  filtros: FiltrosBusqueda = {
    busqueda: '',
    curso: ''
  };

  estadisticas = {
    totalPreguntas: 0,
    cursosDisponibles: 0
  };

  cursosDisponibles: string[] = [];
  cargando = false;

  // Modal properties
  mostrarModal = false;
  editandoPregunta = false;
  preguntaFormulario = {
    enunciado: '',
    curso: ''
  };

  constructor(
    private readonly bancoPreguntaService: BancoPreguntaService,
    private readonly modalService: ModalService
  ) { }

  ngOnInit(): void {
    this.cargarPreguntas();
  }

  cargarPreguntas(): void {
    this.cargando = true;
    this.bancoPreguntaService.obtenerPreguntas().subscribe({
      next: (preguntas: BancoPreguntaDTO[]) => {
        this.preguntas = preguntas;
        this.extraerCursosDisponibles();
        this.aplicarFiltros();
        this.calcularEstadisticas();
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al cargar preguntas:', error);
        this.cargando = false;
        this.modalService.showError(
          'Error al cargar preguntas',
          'No se pudieron cargar las preguntas. Verifique que el backend esté ejecutándose.'
        );
      }
    });
  }

  extraerCursosDisponibles(): void {
    const cursosSet = new Set<string>();
    this.preguntas.forEach(pregunta => {
      if (pregunta.curso) {
        cursosSet.add(pregunta.curso);
      }
    });
    this.cursosDisponibles = Array.from(cursosSet).sort((a, b) => a.localeCompare(b));
  }

  calcularEstadisticas(): void {
    this.estadisticas.totalPreguntas = this.preguntas.length;
    this.estadisticas.cursosDisponibles = this.cursosDisponibles.length;
  }

  aplicarFiltros(): void {
    this.preguntasFiltradas = this.preguntas.filter(pregunta => {
      const coincideBusqueda = !this.filtros.busqueda || 
        pregunta.enunciado.toLowerCase().includes(this.filtros.busqueda.toLowerCase());
      
      const coincideCurso = !this.filtros.curso || 
        pregunta.curso === this.filtros.curso;

      return coincideBusqueda && coincideCurso;
    });
  }

  onFiltroChange(): void {
    this.aplicarFiltros();
  }

  limpiarFiltros(): void {
    this.filtros = {
      busqueda: '',
      curso: ''
    };
    this.aplicarFiltros();
  }

  subirArchivo(event: any): void {
    const archivo = event.target.files[0];
    if (archivo) {
      this.bancoPreguntaService.subirArchivo(archivo).subscribe({
        next: (response: any) => {
          this.modalService.showSuccess(
            'Archivo cargado exitosamente',
            `${response.mensaje}. Registros exitosos: ${response.registrosExitosos}, Errores: ${response.registrosErroneos}`
          );
          this.cargarPreguntas(); // Recargar datos del backend
        },
        error: (error: any) => {
          console.error('Error al cargar archivo:', error);
          this.modalService.showError(
            'Error al cargar archivo',
            error.error?.mensaje ?? error.message
          );
        }
      });
    }
  }

  async limpiarBanco(): Promise<void> {
    const confirmado = await this.modalService.showConfirm(
      'Confirmar limpieza',
      '¿Está seguro de que desea limpiar todo el banco de preguntas? Esta acción no se puede deshacer.',
      'Sí, limpiar',
      'Cancelar'
    );

    if (confirmado) {
      this.bancoPreguntaService.limpiarBanco().subscribe({
        next: () => {
          this.modalService.showSuccess(
            'Banco limpiado',
            'El banco de preguntas ha sido limpiado exitosamente'
          );
          this.cargarPreguntas(); // Recargar desde backend
        },
        error: (error: any) => {
          console.error('Error al limpiar banco:', error);
          this.modalService.showError(
            'Error al limpiar banco',
            'No se pudo limpiar el banco de preguntas'
          );
        }
      });
    }
  }

  // Modal methods
  abrirModalAgregarPregunta(): void {
    this.editandoPregunta = false;
    this.preguntaFormulario = {
      enunciado: '',
      curso: ''
    };
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.editandoPregunta = false;
    this.preguntaFormulario = {
      enunciado: '',
      curso: ''
    };
  }

  guardarPregunta(): void {
    if (!this.preguntaFormulario.enunciado || !this.preguntaFormulario.curso) {
      this.modalService.showWarning(
        'Campos incompletos',
        'Por favor complete todos los campos antes de guardar'
      );
      return;
    }

    // Aquí implementarías la lógica para guardar la pregunta
    console.log('Guardando pregunta:', this.preguntaFormulario);
    this.modalService.showInfo(
      'Funcionalidad en desarrollo',
      'La funcionalidad de agregar pregunta individual está en desarrollo'
    );
    this.cerrarModal();
  }
}