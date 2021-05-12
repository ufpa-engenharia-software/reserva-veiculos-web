import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAvaliacaoSolicitacao, AvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';
import { AvaliacaoSolicitacaoService } from '../service/avaliacao-solicitacao.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISolicitacao } from 'app/entities/solicitacao/solicitacao.model';
import { SolicitacaoService } from 'app/entities/solicitacao/service/solicitacao.service';

@Component({
  selector: 'jhi-avaliacao-solicitacao-update',
  templateUrl: './avaliacao-solicitacao-update.component.html',
})
export class AvaliacaoSolicitacaoUpdateComponent implements OnInit {
  isSaving = false;

  solicitacaosCollection: ISolicitacao[] = [];

  editForm = this.fb.group({
    id: [],
    valorGasolina: [],
    totalGasto: [],
    statusSolicitacao: [],
    justificativaStatus: [],
    solicitacao: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected avaliacaoSolicitacaoService: AvaliacaoSolicitacaoService,
    protected solicitacaoService: SolicitacaoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avaliacaoSolicitacao }) => {
      this.updateForm(avaliacaoSolicitacao);

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
    const avaliacaoSolicitacao = this.createFromForm();
    if (avaliacaoSolicitacao.id !== undefined) {
      this.subscribeToSaveResponse(this.avaliacaoSolicitacaoService.update(avaliacaoSolicitacao));
    } else {
      this.subscribeToSaveResponse(this.avaliacaoSolicitacaoService.create(avaliacaoSolicitacao));
    }
  }

  trackSolicitacaoById(index: number, item: ISolicitacao): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvaliacaoSolicitacao>>): void {
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

  protected updateForm(avaliacaoSolicitacao: IAvaliacaoSolicitacao): void {
    this.editForm.patchValue({
      id: avaliacaoSolicitacao.id,
      valorGasolina: avaliacaoSolicitacao.valorGasolina,
      totalGasto: avaliacaoSolicitacao.totalGasto,
      statusSolicitacao: avaliacaoSolicitacao.statusSolicitacao,
      justificativaStatus: avaliacaoSolicitacao.justificativaStatus,
      solicitacao: avaliacaoSolicitacao.solicitacao,
    });

    this.solicitacaosCollection = this.solicitacaoService.addSolicitacaoToCollectionIfMissing(
      this.solicitacaosCollection,
      avaliacaoSolicitacao.solicitacao
    );
  }

  protected loadRelationshipsOptions(): void {
    this.solicitacaoService
      .query({ 'avaliacaoId.specified': 'false' })
      .pipe(map((res: HttpResponse<ISolicitacao[]>) => res.body ?? []))
      .pipe(
        map((solicitacaos: ISolicitacao[]) =>
          this.solicitacaoService.addSolicitacaoToCollectionIfMissing(solicitacaos, this.editForm.get('solicitacao')!.value)
        )
      )
      .subscribe((solicitacaos: ISolicitacao[]) => (this.solicitacaosCollection = solicitacaos));
  }

  protected createFromForm(): IAvaliacaoSolicitacao {
    return {
      ...new AvaliacaoSolicitacao(),
      id: this.editForm.get(['id'])!.value,
      valorGasolina: this.editForm.get(['valorGasolina'])!.value,
      totalGasto: this.editForm.get(['totalGasto'])!.value,
      statusSolicitacao: this.editForm.get(['statusSolicitacao'])!.value,
      justificativaStatus: this.editForm.get(['justificativaStatus'])!.value,
      solicitacao: this.editForm.get(['solicitacao'])!.value,
    };
  }
}
