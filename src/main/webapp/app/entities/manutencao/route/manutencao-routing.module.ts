import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManutencaoComponent } from '../list/manutencao.component';
import { ManutencaoDetailComponent } from '../detail/manutencao-detail.component';
import { ManutencaoUpdateComponent } from '../update/manutencao-update.component';
import { ManutencaoRoutingResolveService } from './manutencao-routing-resolve.service';

const manutencaoRoute: Routes = [
  {
    path: '',
    component: ManutencaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManutencaoDetailComponent,
    resolve: {
      manutencao: ManutencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManutencaoUpdateComponent,
    resolve: {
      manutencao: ManutencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManutencaoUpdateComponent,
    resolve: {
      manutencao: ManutencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manutencaoRoute)],
  exports: [RouterModule],
})
export class ManutencaoRoutingModule {}
