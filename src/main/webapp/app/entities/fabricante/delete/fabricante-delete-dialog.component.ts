import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';

@Component({
  templateUrl: './fabricante-delete-dialog.component.html',
})
export class FabricanteDeleteDialogComponent {
  fabricante?: IFabricante;

  constructor(protected fabricanteService: FabricanteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fabricanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
