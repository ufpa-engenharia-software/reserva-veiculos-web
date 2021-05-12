import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

@Component({
  templateUrl: './categoria-veiculo-delete-dialog.component.html',
})
export class CategoriaVeiculoDeleteDialogComponent {
  categoriaVeiculo?: ICategoriaVeiculo;

  constructor(protected categoriaVeiculoService: CategoriaVeiculoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaVeiculoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
