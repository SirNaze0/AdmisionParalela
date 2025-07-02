import { Component, OnInit, OnDestroy } from '@angular/core';
import { ModalService, ModalState } from '../../../services/modal.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-global-modal',
  templateUrl: './global-modal.component.html',
  styleUrls: ['./global-modal.component.css']
})
export class GlobalModalComponent implements OnInit, OnDestroy {
  modalState: ModalState = { isOpen: false, config: null };
  private subscription?: Subscription;

  constructor(private modalService: ModalService) { }

  ngOnInit(): void {
    this.subscription = this.modalService.modalState$.subscribe(state => {
      this.modalState = state;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onConfirm(): void {
    if (this.modalState.onConfirm) {
      this.modalState.onConfirm();
    }
  }

  onCancel(): void {
    if (this.modalState.onCancel) {
      this.modalState.onCancel();
    }
  }

  onClose(): void {
    this.modalService.closeModal();
  }
}
