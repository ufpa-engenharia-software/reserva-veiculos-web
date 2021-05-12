import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ManutencaoComponent } from './list/manutencao.component';
import { ManutencaoDetailComponent } from './detail/manutencao-detail.component';
import { ManutencaoUpdateComponent } from './update/manutencao-update.component';
import { ManutencaoDeleteDialogComponent } from './delete/manutencao-delete-dialog.component';
import { ManutencaoRoutingModule } from './route/manutencao-routing.module';

@NgModule({
  imports: [SharedModule, ManutencaoRoutingModule],
  declarations: [ManutencaoComponent, ManutencaoDetailComponent, ManutencaoUpdateComponent, ManutencaoDeleteDialogComponent],
  entryComponents: [ManutencaoDeleteDialogComponent],
})
export class ManutencaoModule {}
