import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// Font Awesome
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GenerarExamenesComponent } from './components/generar-examenes/generar-examenes.component';
import { BancoPreguntasComponent } from './components/banco-preguntas/banco-preguntas.component';
import { CorreccionExamenesComponent } from './components/correccion-examenes/correccion-examenes.component';
import { VerResultadosComponent } from './components/ver-resultados/ver-resultados.component';
import { CargarPostulantesComponent } from './components/cargar-postulantes/cargar-postulantes.component';
import { LayoutComponent } from './shared/layout/layout.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    GenerarExamenesComponent,
    BancoPreguntasComponent,
    CorreccionExamenesComponent,
    VerResultadosComponent,
    CargarPostulantesComponent,
    LayoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    FontAwesomeModule  // Agregar Font Awesome
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }