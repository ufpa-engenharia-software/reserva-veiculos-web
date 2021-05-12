import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SolicitacaoComponent } from './list/solicitacao.component';
import { SolicitacaoDetailComponent } from './detail/solicitacao-detail.component';
import { SolicitacaoUpdateComponent } from './update/solicitacao-update.component';
import { SolicitacaoDeleteDialogComponent } from './delete/solicitacao-delete-dialog.component';
import { SolicitacaoRoutingModule } from './route/solicitacao-routing.module';

@NgModule({
  imports: [SharedModule, SolicitacaoRoutingModule],
  declarations: [SolicitacaoComponent, SolicitacaoDetailComponent, SolicitacaoUpdateComponent, SolicitacaoDeleteDialogComponent],
  entryComponents: [SolicitacaoDeleteDialogComponent],
})
export class SolicitacaoModule {}
