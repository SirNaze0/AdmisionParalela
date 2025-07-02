import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ExamenGeneradoDTO } from './generador.service';

export interface EstadoExamenes {
  examenesGenerados: boolean;
  totalExamenes: number;
  nombreArchivoZip: string;
  urlDescargaZip: string;
  examenesDetalle: ExamenGeneradoDTO[];
  fechaGeneracion: Date;
}

@Injectable({
  providedIn: 'root'
})
export class EstadoGlobalService {
  private readonly estadoExamenesSubject = new BehaviorSubject<EstadoExamenes | null>(null);
  
  constructor() {
    // Recuperar estado del localStorage al inicializar
    this.recuperarEstadoDelLocalStorage();
  }

  get estadoExamenes$(): Observable<EstadoExamenes | null> {
    return this.estadoExamenesSubject.asObservable();
  }

  get estadoExamenesActual(): EstadoExamenes | null {
    return this.estadoExamenesSubject.value;
  }

  actualizarEstadoExamenes(estado: EstadoExamenes): void {
    this.estadoExamenesSubject.next(estado);
    // Guardar en localStorage
    localStorage.setItem('estado_examenes', JSON.stringify(estado));
  }

  limpiarEstadoExamenes(): void {
    this.estadoExamenesSubject.next(null);
    localStorage.removeItem('estado_examenes');
  }

  private recuperarEstadoDelLocalStorage(): void {
    try {
      const estadoGuardado = localStorage.getItem('estado_examenes');
      if (estadoGuardado) {
        const estado = JSON.parse(estadoGuardado);
        // Verificar que el estado no sea muy antiguo (por ejemplo, más de 24 horas)
        const fechaGeneracion = new Date(estado.fechaGeneracion);
        const ahora = new Date();
        const diferenciaMilisegundos = ahora.getTime() - fechaGeneracion.getTime();
        const diferenciaHoras = diferenciaMilisegundos / (1000 * 60 * 60);
        
        if (diferenciaHoras < 24) {
          this.estadoExamenesSubject.next(estado);
        } else {
          // Si es muy antiguo, limpiar el estado
          this.limpiarEstadoExamenes();
        }
      }
    } catch (error) {
      console.error('Error al recuperar estado del localStorage:', error);
      this.limpiarEstadoExamenes();
    }
  }

  // Método para verificar si hay exámenes generados recientemente
  hayExamenesGenerados(): boolean {
    const estado = this.estadoExamenesActual;
    return estado?.examenesGenerados ?? false;
  }
}
