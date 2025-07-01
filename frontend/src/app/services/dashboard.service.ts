import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { BancoPreguntaService } from './banco-pregunta.service';
import { ResultadosService } from './resultados.service';

export interface DashboardStats {
  totalPreguntas: number;
  totalResultados: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private readonly http: HttpClient,
    private readonly bancoPreguntaService: BancoPreguntaService,
    private readonly resultadosService: ResultadosService
  ) { }

  getDashboardStats(): Observable<DashboardStats> {
    return forkJoin({
      preguntas: this.bancoPreguntaService.obtenerPreguntas().pipe(
        catchError(() => of([]))
      ),
      resultados: this.resultadosService.obtenerResultados().pipe(
        catchError(() => of({ totalResultados: 0 }))
      )
    }).pipe(
      map(data => ({
        totalPreguntas: data.preguntas.length,
        totalResultados: data.resultados.totalResultados
      }))
    );
  }
}