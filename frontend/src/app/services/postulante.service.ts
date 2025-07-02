import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DecodificacionService } from './decodificacion.service';

export interface ResultadoCargaDTO {
  mensaje: string;
  registrosExitosos: number;
  registrosErroneos: number;
  detalles: string[];
}

export interface PostulanteDTO {
  postulanteId: number;
  nombres: string;
  apellidos: string;
  dni: string;
  carrera: string;
  area: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostulanteService {
  private readonly baseUrl = 'http://localhost:8080/api/Postulante';

  constructor(
    private readonly http: HttpClient,
    private readonly decodificacionService: DecodificacionService
  ) { }

  subirPostulantes(archivo: File): Observable<ResultadoCargaDTO> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    
    const headers = new HttpHeaders({
      'Accept': 'application/json; charset=utf-8'
    });
    
    return this.http.post<ResultadoCargaDTO>(`${this.baseUrl}/guardar`, formData, { headers });
  }

  limpiarPostulantes(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/limpiar-postulantes`, {});
  }

  contarPostulantes(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/contar`);
  }

  listarPostulantes(): Observable<PostulanteDTO[]> {
    const headers = new HttpHeaders({
      'Accept': 'application/json; charset=utf-8'
    });
    
    return this.http.get<PostulanteDTO[]>(`${this.baseUrl}/listar`, { headers })
      .pipe(
        map((postulantes: PostulanteDTO[]) => 
          postulantes.map(p => this.decodificarTexto(p))
        )
      );
  }

  private decodificarTexto(postulante: PostulanteDTO): PostulanteDTO {
    return {
      ...postulante,
      nombres: this.decodificacionService.corregirCodificacion(postulante.nombres),
      apellidos: this.decodificacionService.corregirCodificacion(postulante.apellidos),
      carrera: this.decodificacionService.corregirCodificacion(postulante.carrera)
    };
  }
}