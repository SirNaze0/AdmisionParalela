import { Component } from '@angular/core';
import { CorreccionService, ResultadoCorreccionDTO } from '../../services/correccion.service';
import { ModalService } from '../../services/modal.service';

// Font Awesome Icons
import { faFileUpload, faChartBar, faBook, faClipboardCheck, faSchool, faFileAlt, faSpinner, faCheckCircle, faExclamationTriangle, faRedo } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-correccion-examenes',
  templateUrl: './correccion-examenes.component.html',
  styleUrls: ['./correccion-examenes.component.css']
})
export class CorreccionExamenesComponent {

  // Font Awesome Icons
  faFileUpload = faFileUpload;
  faChartBar = faChartBar;
  faBook = faBook;
  faClipboardCheck = faClipboardCheck;
  faSchool = faSchool;
  faFileAlt = faFileAlt;
  faSpinner = faSpinner;
  faCheckCircle = faCheckCircle;
  faExclamationTriangle = faExclamationTriangle;
  faRedo = faRedo;

  cargando = false;
  resultado: ResultadoCorreccionDTO | null = null;
  archivoSeleccionado: File | null = null;

  constructor(
    private readonly correccionService: CorreccionService,
    private readonly modalService: ModalService
  ) { }

  onArchivoSeleccionado(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.archivoSeleccionado = input.files[0];
    }
  }

  procesarFichasOpticas(): void {
    if (!this.archivoSeleccionado) {
      this.modalService.showWarning(
        'Archivo requerido',
        'Por favor seleccione un archivo CSV antes de continuar'
      );
      return;
    }

    this.cargando = true;
    this.resultado = null;

    this.correccionService.procesarFichasOpticas(this.archivoSeleccionado).subscribe({
      next: (response: ResultadoCorreccionDTO) => {
        this.resultado = response;
        this.cargando = false;
        console.log('Corrección completada:', response);
        
      },
      error: (error: any) => {
        console.error('Error al procesar fichas ópticas:', error);
        this.cargando = false;
        
        let mensajeError = 'Error al procesar las fichas ópticas: ';
        if (error.error?.mensaje) {
          mensajeError += error.error.mensaje;
        } else if (error.message) {
          mensajeError += error.message;
        } else {
          mensajeError += 'Error desconocido';
        }
        
        mensajeError += '\n\nRevisiones recomendadas:\n' +
                       '• Verifique que el archivo CSV tenga el formato correcto\n' +
                       '• Asegúrese de que los IDs de postulantes y exámenes existan\n' +
                       '• Confirme que haya generado exámenes previamente';
        
        this.modalService.showError(
          'Error en la corrección',
          mensajeError
        );
      }
    });
  }

  limpiarResultado(): void {
    this.resultado = null;
    this.archivoSeleccionado = null;
    // Reset file input
    const fileInput = document.getElementById('archivo-fichas') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}
