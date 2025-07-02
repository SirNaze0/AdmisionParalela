import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
    <app-global-modal></app-global-modal>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'admision-paralela-frontend';
}
