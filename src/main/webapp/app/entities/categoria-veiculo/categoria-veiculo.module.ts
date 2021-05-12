import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CategoriaVeiculoComponent } from './list/categoria-veiculo.component';
import { CategoriaVeiculoDetailComponent } from './detail/categoria-veiculo-detail.component';
import { CategoriaVeiculoUpdateComponent } from './update/categoria-veiculo-update.component';
import { CategoriaVeiculoDeleteDialogComponent } from './delete/categoria-veiculo-delete-dialog.component';
import { CategoriaVeiculoRoutingModule } from './route/categoria-veiculo-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaVeiculoRoutingModule],
  declarations: [
    CategoriaVeiculoComponent,
    CategoriaVeiculoDetailComponent,
    CategoriaVeiculoUpdateComponent,
    CategoriaVeiculoDeleteDialogComponent,
  ],
  entryComponents: [CategoriaVeiculoDeleteDialogComponent],
})
export class CategoriaVeiculoModule {}
