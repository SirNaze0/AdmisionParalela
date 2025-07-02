import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ModalConfig {
  type: 'success' | 'error' | 'warning' | 'info' | 'confirm';
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  showCancel?: boolean;
  autoClose?: boolean;
  autoCloseTime?: number;
}

export interface ModalState {
  isOpen: boolean;
  config: ModalConfig | null;
  onConfirm?: () => void;
  onCancel?: () => void;
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private readonly modalStateSubject = new BehaviorSubject<ModalState>({
    isOpen: false,
    config: null
  });

  modalState$ = this.modalStateSubject.asObservable();

  constructor() { }

  // Método para mostrar un modal de éxito
  showSuccess(title: string, message: string, autoClose: boolean = true): Promise<void> {
    return this.showModal({
      type: 'success',
      title,
      message,
      confirmText: 'Aceptar',
      autoClose,
      autoCloseTime: 3000
    });
  }

  // Método para mostrar un modal de error
  showError(title: string, message: string): Promise<void> {
    return this.showModal({
      type: 'error',
      title,
      message,
      confirmText: 'Aceptar'
    });
  }

  // Método para mostrar un modal de advertencia
  showWarning(title: string, message: string): Promise<void> {
    return this.showModal({
      type: 'warning',
      title,
      message,
      confirmText: 'Aceptar'
    });
  }

  // Método para mostrar un modal de información
  showInfo(title: string, message: string): Promise<void> {
    return this.showModal({
      type: 'info',
      title,
      message,
      confirmText: 'Aceptar'
    });
  }

  // Método para mostrar un modal de confirmación
  showConfirm(title: string, message: string, confirmText: string = 'Confirmar', cancelText: string = 'Cancelar'): Promise<boolean> {
    return new Promise((resolve) => {
      const config: ModalConfig = {
        type: 'confirm',
        title,
        message,
        confirmText,
        cancelText,
        showCancel: true
      };

      this.modalStateSubject.next({
        isOpen: true,
        config,
        onConfirm: () => {
          this.closeModal();
          resolve(true);
        },
        onCancel: () => {
          this.closeModal();
          resolve(false);
        }
      });
    });
  }

  // Método genérico para mostrar cualquier tipo de modal
  private showModal(config: ModalConfig): Promise<void> {
    return new Promise((resolve) => {
      this.modalStateSubject.next({
        isOpen: true,
        config,
        onConfirm: () => {
          this.closeModal();
          resolve();
        }
      });
    });
  }

  // Método para cerrar el modal
  closeModal(): void {
    this.modalStateSubject.next({
      isOpen: false,
      config: null
    });
  }

  // Método de conveniencia para reemplazar alerts simples
  alert(message: string, title: string = 'Información'): Promise<void> {
    return this.showInfo(title, message);
  }

  // Método de conveniencia para reemplazar confirms
  confirm(message: string, title: string = 'Confirmación'): Promise<boolean> {
    return this.showConfirm(title, message);
  }
}
