import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { VeiculoComponent } from './list/veiculo.component';
import { VeiculoDetailComponent } from './detail/veiculo-detail.component';
import { VeiculoUpdateComponent } from './update/veiculo-update.component';
import { VeiculoDeleteDialogComponent } from './delete/veiculo-delete-dialog.component';
import { VeiculoRoutingModule } from './route/veiculo-routing.module';

@NgModule({
  imports: [SharedModule, VeiculoRoutingModule],
  declarations: [VeiculoComponent, VeiculoDetailComponent, VeiculoUpdateComponent, VeiculoDeleteDialogComponent],
  entryComponents: [VeiculoDeleteDialogComponent],
})
export class VeiculoModule {}
