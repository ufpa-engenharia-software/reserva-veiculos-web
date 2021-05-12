import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FabricanteComponent } from '../list/fabricante.component';
import { FabricanteDetailComponent } from '../detail/fabricante-detail.component';
import { FabricanteUpdateComponent } from '../update/fabricante-update.component';
import { FabricanteRoutingResolveService } from './fabricante-routing-resolve.service';

const fabricanteRoute: Routes = [
  {
    path: '',
    component: FabricanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FabricanteDetailComponent,
    resolve: {
      fabricante: FabricanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FabricanteUpdateComponent,
    resolve: {
      fabricante: FabricanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FabricanteUpdateComponent,
    resolve: {
      fabricante: FabricanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fabricanteRoute)],
  exports: [RouterModule],
})
export class FabricanteRoutingModule {}
