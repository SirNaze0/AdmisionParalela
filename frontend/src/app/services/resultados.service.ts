import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TablaResultadosDTO {
  mensaje?: string;
  totalResultados: number;
  resultados?: any[]; // La estructura exacta depende del backend
}

@Injectable({
  providedIn: 'root'
})
export class ResultadosService {
  private readonly baseUrl = 'http://localhost:8080/api/resultados';

  constructor(private readonly http: HttpClient) { }

  obtenerResultados(area?: string): Observable<TablaResultadosDTO> {
    const url = area ? `${this.baseUrl}/tabla?area=${area}` : `${this.baseUrl}/tabla`;
    return this.http.get<TablaResultadosDTO>(url);
  }

  descargarPDF(area?: string): Observable<Blob> {
    const url = area ? `${this.baseUrl}/pdf?area=${area}` : `${this.baseUrl}/pdf`;
    return this.http.get(url, { responseType: 'blob' });
  }
}