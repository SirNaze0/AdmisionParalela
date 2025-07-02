import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { EstadoGlobalService } from '../../services/estado-global.service';
import { Subscription } from 'rxjs';

// Font Awesome Icons
import { 
  faChartBar, 
  faFileAlt, 
  faUsers, 
  faBook, 
  faClipboardCheck, 
  faSchool,
  faChartLine,
  faCheckCircle
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit, OnDestroy {

  // Font Awesome Icons
  faSchool = faSchool;
  faChartBar = faChartBar;
  faFileAlt = faFileAlt;
  faUsers = faUsers;
  faBook = faBook;
  faClipboardCheck = faClipboardCheck;
  faChartLine = faChartLine;
  faCheckCircle = faCheckCircle;

  hayExamenesGenerados = false;
  private estadoSubscription?: Subscription;

  constructor(
    private readonly router: Router,
    private readonly estadoGlobalService: EstadoGlobalService
  ) { }

  ngOnInit(): void {
    this.estadoSubscription = this.estadoGlobalService.estadoExamenes$.subscribe(estado => {
      this.hayExamenesGenerados = estado?.examenesGenerados ?? false;
    });
  }

  ngOnDestroy(): void {
    if (this.estadoSubscription) {
      this.estadoSubscription.unsubscribe();
    }
  }

  isActive(route: string): boolean {
    return this.router.url === route || this.router.url.startsWith(route + '/');
  }
}
