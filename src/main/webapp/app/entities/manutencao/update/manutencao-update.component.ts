import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IManutencao, Manutencao } from '../manutencao.model';
import { ManutencaoService } from '../service/manutencao.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';

@Component({
  selector: 'jhi-manutencao-update',
  templateUrl: './manutencao-update.component.html',
})
export class ManutencaoUpdateComponent implements OnInit {
  isSaving = false;

  veiculosSharedCollection: IVeiculo[] = [];

  editForm = this.fb.group({
    id: [],
    km: [],
    descricao: [],
    custo: [],
    veiculo: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected manutencaoService: ManutencaoService,
    protected veiculoService: VeiculoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manutencao }) => {
      this.updateForm(manutencao);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('reservaVeiculosApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manutencao = this.createFromForm();
    if (manutencao.id !== undefined) {
      this.subscribeToSaveResponse(this.manutencaoService.update(manutencao));
    } else {
      this.subscribeToSaveResponse(this.manutencaoService.create(manutencao));
    }
  }

  trackVeiculoById(index: number, item: IVeiculo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManutencao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(manutencao: IManutencao): void {
    this.editForm.patchValue({
      id: manutencao.id,
      km: manutencao.km,
      descricao: manutencao.descricao,
      custo: manutencao.custo,
      veiculo: manutencao.veiculo,
    });

    this.veiculosSharedCollection = this.veiculoService.addVeiculoToCollectionIfMissing(this.veiculosSharedCollection, manutencao.veiculo);
  }

  protected loadRelationshipsOptions(): void {
    this.veiculoService
      .query()
      .pipe(map((res: HttpResponse<IVeiculo[]>) => res.body ?? []))
      .pipe(
        map((veiculos: IVeiculo[]) => this.veiculoService.addVeiculoToCollectionIfMissing(veiculos, this.editForm.get('veiculo')!.value))
      )
      .subscribe((veiculos: IVeiculo[]) => (this.veiculosSharedCollection = veiculos));
  }

  protected createFromForm(): IManutencao {
    return {
      ...new Manutencao(),
      id: this.editForm.get(['id'])!.value,
      km: this.editForm.get(['km'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      custo: this.editForm.get(['custo'])!.value,
      veiculo: this.editForm.get(['veiculo'])!.value,
    };
  }
}
