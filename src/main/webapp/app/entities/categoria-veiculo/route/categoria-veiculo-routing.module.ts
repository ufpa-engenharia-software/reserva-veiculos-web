import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaVeiculoComponent } from '../list/categoria-veiculo.component';
import { CategoriaVeiculoDetailComponent } from '../detail/categoria-veiculo-detail.component';
import { CategoriaVeiculoUpdateComponent } from '../update/categoria-veiculo-update.component';
import { CategoriaVeiculoRoutingResolveService } from './categoria-veiculo-routing-resolve.service';

const categoriaVeiculoRoute: Routes = [
  {
    path: '',
    component: CategoriaVeiculoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaVeiculoDetailComponent,
    resolve: {
      categoriaVeiculo: CategoriaVeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaVeiculoUpdateComponent,
    resolve: {
      categoriaVeiculo: CategoriaVeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaVeiculoUpdateComponent,
    resolve: {
      categoriaVeiculo: CategoriaVeiculoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaVeiculoRoute)],
  exports: [RouterModule],
})
export class CategoriaVeiculoRoutingModule {}
