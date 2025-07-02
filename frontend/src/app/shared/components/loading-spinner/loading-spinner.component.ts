import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  templateUrl: './loading-spinner.component.html',
  styleUrls: ['./loading-spinner.component.css']
})
export class LoadingSpinnerComponent {
  @Input() message = 'Cargando...';
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() overlay = false;
}
