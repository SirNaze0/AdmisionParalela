import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BancoPreguntaDTO {
  enunciado: string;
  curso: string;
}

export interface ResultadoCargaDTO {
  mensaje: string;
  registrosExitosos: number;
  registrosErroneos: number;
  detalles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class BancoPreguntaService {
  private readonly baseUrl = 'http://localhost:8080/api/banco';

  constructor(private readonly http: HttpClient) { }

  obtenerPreguntas(): Observable<BancoPreguntaDTO[]> {
    return this.http.get<BancoPreguntaDTO[]>(this.baseUrl);
  }

  subirArchivo(archivo: File): Observable<ResultadoCargaDTO> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ResultadoCargaDTO>(`${this.baseUrl}/guardar`, formData);
  }

  limpiarBanco(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar-bpbr`, {});
  }
}