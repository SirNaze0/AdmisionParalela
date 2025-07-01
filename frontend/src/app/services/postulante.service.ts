import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ResultadoCargaDTO {
  mensaje: string;
  registrosExitosos: number;
  registrosErroneos: number;
  detalles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class PostulanteService {
  private readonly baseUrl = 'http://localhost:8080/api/Postulante';

  constructor(private readonly http: HttpClient) { }

  subirPostulantes(archivo: File): Observable<ResultadoCargaDTO> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ResultadoCargaDTO>(`${this.baseUrl}/guardar`, formData);
  }

  limpiarPostulantes(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar-postulantes`, {});
  }
}