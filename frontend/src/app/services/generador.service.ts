import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DecodificacionService } from './decodificacion.service';

export interface ArchivoGeneradoDTO {
  [key: string]: any; // La estructura exacta depende del backend
}

export interface ExamenGeneradoDTO {
  examenId: number;
  postulanteId: number;
  nombresPostulante: string;
  apellidosPostulante: string;
  dni: string;
  carrera: string;
  area: string;
  rutaPdf: string;
  nombreArchivoPdf: string;
  urlDescarga: string;
}

@Injectable({
  providedIn: 'root'
})
export class GeneradorService {
  private readonly baseUrl = 'http://localhost:8080/api/generador';

  constructor(
    private readonly http: HttpClient,
    private readonly decodificacionService: DecodificacionService
  ) { }

  generarExamenes(cantidad: number): Observable<ArchivoGeneradoDTO> {
    return this.http.get<ArchivoGeneradoDTO>(`${this.baseUrl}/generar?cantidad=${cantidad}`);
  }

  limpiarExamenes(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar`, {});
  }

  limpiarBaseDatosCompleta(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar-todo`, {});
  }

  obtenerExamenesDetalle(): Observable<ExamenGeneradoDTO[]> {
    const headers = new HttpHeaders({
      'Accept': 'application/json; charset=utf-8'
    });
    
    return this.http.get<ExamenGeneradoDTO[]>(`${this.baseUrl}/examenes-detalle`, { headers })
      .pipe(
        map((examenes: ExamenGeneradoDTO[]) => 
          examenes.map(e => this.decodificarTextoExamen(e))
        )
      );
  }

  descargarExamenIndividual(examenId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/descargar-examen/${examenId}`, { responseType: 'blob' });
  }

  private decodificarTextoExamen(examen: ExamenGeneradoDTO): ExamenGeneradoDTO {
    return {
      ...examen,
      nombresPostulante: this.decodificacionService.corregirCodificacion(examen.nombresPostulante),
      apellidosPostulante: this.decodificacionService.corregirCodificacion(examen.apellidosPostulante),
      carrera: this.decodificacionService.corregirCodificacion(examen.carrera)
    };
  }
}