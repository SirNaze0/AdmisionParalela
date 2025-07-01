import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GenerarExamenesComponent } from './components/generar-examenes/generar-examenes.component';
import { BancoPreguntasComponent } from './components/banco-preguntas/banco-preguntas.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'generar-examenes', component: GenerarExamenesComponent },
  { path: 'banco-preguntas', component: BancoPreguntasComponent },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
