import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AvaliacaoSolicitacaoComponent } from './list/avaliacao-solicitacao.component';
import { AvaliacaoSolicitacaoDetailComponent } from './detail/avaliacao-solicitacao-detail.component';
import { AvaliacaoSolicitacaoUpdateComponent } from './update/avaliacao-solicitacao-update.component';
import { AvaliacaoSolicitacaoDeleteDialogComponent } from './delete/avaliacao-solicitacao-delete-dialog.component';
import { AvaliacaoSolicitacaoRoutingModule } from './route/avaliacao-solicitacao-routing.module';

@NgModule({
  imports: [SharedModule, AvaliacaoSolicitacaoRoutingModule],
  declarations: [
    AvaliacaoSolicitacaoComponent,
    AvaliacaoSolicitacaoDetailComponent,
    AvaliacaoSolicitacaoUpdateComponent,
    AvaliacaoSolicitacaoDeleteDialogComponent,
  ],
  entryComponents: [AvaliacaoSolicitacaoDeleteDialogComponent],
})
export class AvaliacaoSolicitacaoModule {}
