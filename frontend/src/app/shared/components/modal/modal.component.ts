import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, OnChanges } from '@angular/core';
import { faTimes, faCheckCircle, faExclamationTriangle, faInfoCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';

export type ModalType = 'success' | 'error' | 'warning' | 'info' | 'confirm';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit, OnDestroy, OnChanges {
  @Input() isOpen = false;
  @Input() type: ModalType = 'info';
  @Input() title = '';
  @Input() message = '';
  @Input() confirmText = 'Aceptar';
  @Input() cancelText = 'Cancelar';
  @Input() showCancel = false;
  @Input() autoClose = false;
  @Input() autoCloseTime = 3000;

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();

  // Font Awesome Icons
  faTimes = faTimes;
  faCheckCircle = faCheckCircle;
  faExclamationTriangle = faExclamationTriangle;
  faInfoCircle = faInfoCircle;
  faTimesCircle = faTimesCircle;

  private autoCloseTimeout?: number;

  ngOnInit(): void {
    if (this.autoClose && this.isOpen) {
      this.startAutoClose();
    }
  }

  ngOnDestroy(): void {
    if (this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
    }
  }

  ngOnChanges(): void {
    if (this.isOpen && this.autoClose && this.type !== 'confirm') {
      this.startAutoClose();
    } else if (!this.isOpen && this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
    }
  }

  private startAutoClose(): void {
    if (this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
    }
    
    this.autoCloseTimeout = window.setTimeout(() => {
      this.onClose();
    }, this.autoCloseTime);
  }

  onConfirm(): void {
    this.confirm.emit();
    this.onClose();
  }

  onCancel(): void {
    this.cancel.emit();
    this.onClose();
  }

  onClose(): void {
    if (this.autoCloseTimeout) {
      clearTimeout(this.autoCloseTimeout);
    }
    this.close.emit();
  }

  onBackdropClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }

  getModalIcon(): any {
    switch (this.type) {
      case 'success': return this.faCheckCircle;
      case 'error': return this.faTimesCircle;
      case 'warning': return this.faExclamationTriangle;
      case 'info': return this.faInfoCircle;
      case 'confirm': return this.faExclamationTriangle;
      default: return this.faInfoCircle;
    }
  }

  getModalIconClass(): string {
    switch (this.type) {
      case 'success': return 'icon-success';
      case 'error': return 'icon-error';
      case 'warning': return 'icon-warning';
      case 'info': return 'icon-info';
      case 'confirm': return 'icon-warning';
      default: return 'icon-info';
    }
  }
}
