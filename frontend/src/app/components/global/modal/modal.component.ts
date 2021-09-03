import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
})
export class ModalComponent {

  @Input() displayModal: boolean = false;
  @Input() classType: string = "bg-danger";
  @Input() modalTitle: string;
  @Input() okButton: string = "common.modal.delete";
  @Input() size: string = "md";
  @Input() displayOkButton: boolean = true;
  @Output() toggleModal = new EventEmitter<boolean>();
  @Output() execute = new EventEmitter<void>();

  toggleModalAction() {
    this.toggleModal.emit(!this.displayModal);
  }

  executeAction() {
    this.execute.emit();
    this.toggleModalAction();
  }

}
