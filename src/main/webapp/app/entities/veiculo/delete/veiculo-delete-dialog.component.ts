import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVeiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';

@Component({
  templateUrl: './veiculo-delete-dialog.component.html',
})
export class VeiculoDeleteDialogComponent {
  veiculo?: IVeiculo;

  constructor(protected veiculoService: VeiculoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.veiculoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
