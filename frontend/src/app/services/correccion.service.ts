import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ResultadoCorreccionDTO {
  mensaje: string;
  examenesCorregidos: number;
  errores: number;
}

@Injectable({
  providedIn: 'root'
})
export class CorreccionService {
  private readonly baseUrl = 'http://localhost:8080/api/correccion';

  constructor(private readonly http: HttpClient) { }

  procesarFichasOpticas(archivo: File): Observable<ResultadoCorreccionDTO> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ResultadoCorreccionDTO>(`${this.baseUrl}/procesar`, formData);
  }
}