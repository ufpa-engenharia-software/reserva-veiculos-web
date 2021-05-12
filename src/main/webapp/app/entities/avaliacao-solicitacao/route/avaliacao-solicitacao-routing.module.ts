import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AvaliacaoSolicitacaoComponent } from '../list/avaliacao-solicitacao.component';
import { AvaliacaoSolicitacaoDetailComponent } from '../detail/avaliacao-solicitacao-detail.component';
import { AvaliacaoSolicitacaoUpdateComponent } from '../update/avaliacao-solicitacao-update.component';
import { AvaliacaoSolicitacaoRoutingResolveService } from './avaliacao-solicitacao-routing-resolve.service';

const avaliacaoSolicitacaoRoute: Routes = [
  {
    path: '',
    component: AvaliacaoSolicitacaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvaliacaoSolicitacaoDetailComponent,
    resolve: {
      avaliacaoSolicitacao: AvaliacaoSolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvaliacaoSolicitacaoUpdateComponent,
    resolve: {
      avaliacaoSolicitacao: AvaliacaoSolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvaliacaoSolicitacaoUpdateComponent,
    resolve: {
      avaliacaoSolicitacao: AvaliacaoSolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(avaliacaoSolicitacaoRoute)],
  exports: [RouterModule],
})
export class AvaliacaoSolicitacaoRoutingModule {}
