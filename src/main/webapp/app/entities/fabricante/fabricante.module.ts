import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FabricanteComponent } from './list/fabricante.component';
import { FabricanteDetailComponent } from './detail/fabricante-detail.component';
import { FabricanteUpdateComponent } from './update/fabricante-update.component';
import { FabricanteDeleteDialogComponent } from './delete/fabricante-delete-dialog.component';
import { FabricanteRoutingModule } from './route/fabricante-routing.module';

@NgModule({
  imports: [SharedModule, FabricanteRoutingModule],
  declarations: [FabricanteComponent, FabricanteDetailComponent, FabricanteUpdateComponent, FabricanteDeleteDialogComponent],
  entryComponents: [FabricanteDeleteDialogComponent],
})
export class FabricanteModule {}
