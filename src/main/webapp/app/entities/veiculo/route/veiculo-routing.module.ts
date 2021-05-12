import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VeiculoComponent } from '../list/veiculo.component';
import { VeiculoDetailComponent } from '../detail/veiculo-detail.component';
import { VeiculoUpdateComponent } from '../update/veiculo-update.component';
import { VeiculoRoutingResolveService } from './veiculo-routing-resolve.service';

const veiculoRoute: Routes = [
  {
    path: '',
    component: VeiculoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VeiculoDetailComponent,
    resolve: {
      veiculo: VeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VeiculoUpdateComponent,
    resolve: {
      veiculo: VeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VeiculoUpdateComponent,
    resolve: {
      veiculo: VeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(veiculoRoute)],
  exports: [RouterModule],
})
export class VeiculoRoutingModule {}
