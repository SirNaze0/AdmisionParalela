import { Component, OnInit } from '@angular/core';
import { DashboardService, DashboardStats } from '../../services/dashboard.service';

// Font Awesome Icons
import { faChartBar, faUser, faUsers, faStar, faFileAlt, faSchool, faCog, faDownload, faBook, faClipboardCheck } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // Font Awesome Icons
  faChartBar = faChartBar;
  faUser = faUser;
  faUsers = faUsers;
  faStar = faStar;
  faFileAlt = faFileAlt;
  faSchool = faSchool;
  faCog = faCog;
  faDownload = faDownload;
  faBook = faBook;
  faClipboardCheck = faClipboardCheck;

  stats: DashboardStats = {
    totalPreguntas: 0,
    totalResultados: 0
  };
  
  cargando = false;

  constructor(private dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.cargando = true;
    this.dashboardService.getDashboardStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar datos del dashboard:', error);
        this.cargando = false;
      }
    });
  }
}