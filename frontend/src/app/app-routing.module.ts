import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GenerarExamenesComponent } from './components/generar-examenes/generar-examenes.component';
import { BancoPreguntasComponent } from './components/banco-preguntas/banco-preguntas.component';
import { CorreccionExamenesComponent } from './components/correccion-examenes/correccion-examenes.component';
import { VerResultadosComponent } from './components/ver-resultados/ver-resultados.component';
import { CargarPostulantesComponent } from './components/cargar-postulantes/cargar-postulantes.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'cargar-postulantes', component: CargarPostulantesComponent },
  { path: 'generar-examenes', component: GenerarExamenesComponent },
  { path: 'banco-preguntas', component: BancoPreguntasComponent },
  { path: 'correccion-examenes', component: CorreccionExamenesComponent },
  { path: 'resultados', component: VerResultadosComponent },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
