import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';
import { AvaliacaoSolicitacaoService } from '../service/avaliacao-solicitacao.service';

@Component({
  templateUrl: './avaliacao-solicitacao-delete-dialog.component.html',
})
export class AvaliacaoSolicitacaoDeleteDialogComponent {
  avaliacaoSolicitacao?: IAvaliacaoSolicitacao;

  constructor(protected avaliacaoSolicitacaoService: AvaliacaoSolicitacaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.avaliacaoSolicitacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
