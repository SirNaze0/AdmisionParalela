import { Component } from '@angular/core';
import { CorreccionService, ResultadoCorreccionDTO } from '../../services/correccion.service';

// Font Awesome Icons
import { faFileUpload, faChartBar, faBook, faClipboardCheck, faSchool, faFileAlt, faSpinner, faCheckCircle, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';

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

  cargando = false;
  resultado: ResultadoCorreccionDTO | null = null;
  archivoSeleccionado: File | null = null;

  constructor(private readonly correccionService: CorreccionService) { }

  onArchivoSeleccionado(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.archivoSeleccionado = input.files[0];
    }
  }

  procesarFichasOpticas(): void {
    if (!this.archivoSeleccionado) {
      alert('Por favor seleccione un archivo CSV');
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
        
        alert(mensajeError);
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
