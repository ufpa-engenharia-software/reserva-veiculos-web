import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SolicitacaoComponent } from '../list/solicitacao.component';
import { SolicitacaoDetailComponent } from '../detail/solicitacao-detail.component';
import { SolicitacaoUpdateComponent } from '../update/solicitacao-update.component';
import { SolicitacaoRoutingResolveService } from './solicitacao-routing-resolve.service';

const solicitacaoRoute: Routes = [
  {
    path: '',
    component: SolicitacaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SolicitacaoDetailComponent,
    resolve: {
      solicitacao: SolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SolicitacaoUpdateComponent,
    resolve: {
      solicitacao: SolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SolicitacaoUpdateComponent,
    resolve: {
      solicitacao: SolicitacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(solicitacaoRoute)],
  exports: [RouterModule],
})
export class SolicitacaoRoutingModule {}
