import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ArchivoGeneradoDTO {
  [key: string]: any; // La estructura exacta depende del backend
}

@Injectable({
  providedIn: 'root'
})
export class GeneradorService {
  private readonly baseUrl = 'http://localhost:8080/api/generador';

  constructor(private readonly http: HttpClient) { }

  generarExamenes(cantidad: number): Observable<ArchivoGeneradoDTO> {
    return this.http.get<ArchivoGeneradoDTO>(`${this.baseUrl}/generar?cantidad=${cantidad}`);
  }

  limpiarExamenes(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar`, {});
  }
}