import { Component, OnInit } from '@angular/core';
import { ResultadosService, TablaResultadosDTO } from '../../services/resultados.service';

// Font Awesome Icons
import { faTable, faChartBar, faBook, faClipboardCheck, faSchool, faFileAlt, faDownload, faFilter, faSpinner, faFileExcel } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-ver-resultados',
  templateUrl: './ver-resultados.component.html',
  styleUrls: ['./ver-resultados.component.css']
})
export class VerResultadosComponent implements OnInit {

  // Font Awesome Icons
  faTable = faTable;
  faChartBar = faChartBar;
  faBook = faBook;
  faClipboardCheck = faClipboardCheck;
  faSchool = faSchool;
  faFileAlt = faFileAlt;
  faDownload = faDownload;
  faFilter = faFilter;
  faSpinner = faSpinner;
  faFileExcel = faFileExcel;

  cargando = false;
  descargandoPDF = false;
  resultados: TablaResultadosDTO | null = null;
  areaSeleccionada = '';
  areas = ['A', 'B', 'C', 'D', 'E'];

  constructor(private readonly resultadosService: ResultadosService) { }

  ngOnInit(): void {
    this.cargarResultados();
  }

  cargarResultados(): void {
    this.cargando = true;
    const area = this.areaSeleccionada || undefined;
    
    this.resultadosService.obtenerResultados(area).subscribe({
      next: (response: TablaResultadosDTO) => {
        this.resultados = response;
        this.cargando = false;
        console.log('Resultados cargados:', response);
      },
      error: (error: any) => {
        console.error('Error al cargar resultados:', error);
        this.cargando = false;
        alert('Error al cargar los resultados: ' + (error.error?.mensaje ?? error.message));
      }
    });
  }

  onAreaChange(): void {
    this.cargarResultados();
  }

  descargarPDF(): void {
    this.descargandoPDF = true;
    const area = this.areaSeleccionada || undefined;
    
    this.resultadosService.descargarPDF(area).subscribe({
      next: (blob: Blob) => {
        // Crear URL para el blob y descargar
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = area ? `Resultados_Area_${area}.pdf` : 'Resultados_Todos.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        
        this.descargandoPDF = false;
      },
      error: (error: any) => {
        console.error('Error al descargar PDF:', error);
        this.descargandoPDF = false;
        alert('Error al descargar el PDF: ' + (error.error?.mensaje ?? error.message));
      }
    });
  }

  limpiarFiltros(): void {
    this.areaSeleccionada = '';
    this.cargarResultados();
  }
}
