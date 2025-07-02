import { Component, OnInit } from '@angular/core';
import { PostulanteService, ResultadoCargaDTO, PostulanteDTO } from '../../services/postulante.service';
import { ModalService } from '../../services/modal.service';
import { faSchool, faChartBar, faFileAlt, faBook, faClipboardCheck, faFileUpload, faSpinner, faCheckCircle, faExclamationTriangle, faTrash, faUsers, faEye, faDownload, faEyeSlash, faFileDownload, faInfoCircle, faRedo, faFilter, faUser, faIdCard } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-cargar-postulantes',
  templateUrl: './cargar-postulantes.component.html',
  styleUrls: ['./cargar-postulantes.component.css']
})
export class CargarPostulantesComponent implements OnInit {
  // Iconos
  faSchool = faSchool;
  faChartBar = faChartBar;
  faFileAlt = faFileAlt;
  faBook = faBook;
  faClipboardCheck = faClipboardCheck;
  faFileUpload = faFileUpload;
  faSpinner = faSpinner;
  faCheckCircle = faCheckCircle;
  faExclamationTriangle = faExclamationTriangle;
  faTrash = faTrash;
  faUsers = faUsers;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  faFileDownload = faFileDownload;
  faInfoCircle = faInfoCircle;
  faRedo = faRedo;
  faFilter = faFilter;
  faUser = faUser;
  faIdCard = faIdCard;

  // Estado del componente
  archivoSeleccionado: File | null = null;
  cargando = false;
  resultado: ResultadoCargaDTO | null = null;
  postulantes: PostulanteDTO[] = [];
  postulantesFiltrados: PostulanteDTO[] = [];
  mostrandoPostulantes = false;

  // Filtros
  filtroAreaSeleccionada = '';
  areasDisponibles: string[] = [];

  constructor(
    private readonly postulanteService: PostulanteService,
    private readonly modalService: ModalService
  ) { }

  ngOnInit(): void {
    // Cargar postulantes existentes al inicializar
    this.cargarListaPostulantes();
  }

  cargarListaPostulantes(): void {
    this.postulanteService.listarPostulantes().subscribe({
      next: (postulantes: PostulanteDTO[]) => {
        this.postulantes = postulantes;
        this.actualizarAreasDisponibles();
        this.aplicarFiltros();
        this.mostrandoPostulantes = postulantes.length > 0;
      },
      error: (error: any) => {
        console.error('Error al cargar lista de postulantes:', error);
        this.postulantes = [];
        this.postulantesFiltrados = [];
        this.areasDisponibles = [];
        this.mostrandoPostulantes = false;
      }
    });
  }

  actualizarAreasDisponibles(): void {
    const areas = new Set(this.postulantes.map(p => p.area));
    this.areasDisponibles = Array.from(areas).sort((a, b) => a.localeCompare(b));
  }

  aplicarFiltros(): void {
    let filtrados = [...this.postulantes];

    // Filtrar por área si está seleccionada
    if (this.filtroAreaSeleccionada) {
      filtrados = filtrados.filter(p => p.area === this.filtroAreaSeleccionada);
    }

    this.postulantesFiltrados = filtrados;
  }

  limpiarFiltros(): void {
    this.filtroAreaSeleccionada = '';
    this.aplicarFiltros();
  }

  onArchivoSeleccionado(event: any): void {
    const archivo = event.target.files[0];
    if (archivo) {
      this.archivoSeleccionado = archivo;
      this.resultado = null; // Limpiar resultado anterior
    }
  }

  cargarPostulantes(): void {
    if (!this.archivoSeleccionado) {
      this.modalService.showWarning(
        'Archivo requerido',
        'Por favor seleccione un archivo CSV antes de continuar'
      );
      return;
    }

    this.cargando = true;
    this.resultado = null;

    this.postulanteService.subirPostulantes(this.archivoSeleccionado).subscribe({
      next: (response: ResultadoCargaDTO) => {
        this.resultado = response;
        this.cargando = false;
        this.archivoSeleccionado = null;
        
        // Recargar la lista de postulantes después de cargar exitosamente
        this.cargarListaPostulantes();
        
        // Reset file input
        const fileInput = document.getElementById('archivo-postulantes') as HTMLInputElement;
        if (fileInput) {
          fileInput.value = '';
        }
      },
      error: (error: any) => {
        console.error('Error al cargar postulantes:', error);
        this.resultado = {
          mensaje: 'Error al procesar archivo: ' + (error.error?.mensaje ?? error.message),
          registrosExitosos: 0,
          registrosErroneos: 1,
          detalles: []
        };
        this.cargando = false;
      }
    });
  }

  async limpiarPostulantes(): Promise<void> {
    const confirmado = await this.modalService.showConfirm(
      'Confirmar eliminación',
      '¿Está seguro de que desea eliminar TODOS los postulantes? Esta acción no se puede deshacer.',
      'Sí, eliminar todo',
      'Cancelar'
    );

    if (!confirmado) {
      return;
    }

    this.cargando = true;

    this.postulanteService.limpiarPostulantes().subscribe({
      next: () => {
        this.modalService.showSuccess(
          'Postulantes eliminados',
          'Todos los postulantes han sido eliminados correctamente'
        );
        this.cargando = false;
        this.resultado = null;
        this.cargarListaPostulantes(); // Recargar lista vacía
      },
      error: (error: any) => {
        console.error('Error al limpiar postulantes:', error);
        this.modalService.showError(
          'Error al limpiar postulantes',
          error.error?.mensaje ?? error.message
        );
        this.cargando = false;
      }
    });
  }

  limpiarResultado(): void {
    this.resultado = null;
    this.archivoSeleccionado = null;
  }

  toggleMostrarPostulantes(): void {
    this.mostrandoPostulantes = !this.mostrandoPostulantes;
  }

  generarCsvFichasOpticas(): void {
    if (this.postulantes.length === 0) {
      this.modalService.showWarning(
        'No hay postulantes',
        'No hay postulantes cargados para generar el CSV'
      );
      return;
    }

    // Crear contenido del CSV
    let csvContent = 'codigoPostulante,idExamen,respuestas\n';
    
    this.postulantes.forEach(postulante => {
      // Generar respuestas de ejemplo (el usuario las reemplazará)
      const respuestasEjemplo = 'ABCD*'; // 5 respuestas de ejemplo
      csvContent += `${postulante.postulanteId},${postulante.postulanteId},${respuestasEjemplo}\n`;
    });

    // Descargar el archivo
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'plantilla_fichas_opticas.csv';
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }

  trackByPostulanteId(index: number, postulante: PostulanteDTO): number {
    return postulante.postulanteId;
  }
}
