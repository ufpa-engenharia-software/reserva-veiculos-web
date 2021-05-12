import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManutencao } from '../manutencao.model';
import { ManutencaoService } from '../service/manutencao.service';

@Component({
  templateUrl: './manutencao-delete-dialog.component.html',
})
export class ManutencaoDeleteDialogComponent {
  manutencao?: IManutencao;

  constructor(protected manutencaoService: ManutencaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manutencaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
