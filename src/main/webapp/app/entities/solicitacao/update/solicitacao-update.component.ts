import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISolicitacao, Solicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-solicitacao-update',
  templateUrl: './solicitacao-update.component.html',
})
export class SolicitacaoUpdateComponent implements OnInit {
  isSaving = false;

  categoriaVeiculosSharedCollection: ICategoriaVeiculo[] = [];
  veiculosSharedCollection: IVeiculo[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    origem: [],
    destino: [],
    dataSolicitacao: [],
    horarioSaida: [],
    horarioRetorno: [],
    distanciaEstimadaKm: [],
    justificativa: [],
    status: [],
    nPessoas: [],
    peso: [],
    categoria: [],
    veiculoAlocado: [],
    solicitante: [],
    autorizador: [],
    motorista: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected solicitacaoService: SolicitacaoService,
    protected categoriaVeiculoService: CategoriaVeiculoService,
    protected veiculoService: VeiculoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solicitacao }) => {
      if (solicitacao.id === undefined) {
        const today = dayjs().startOf('day');
        solicitacao.dataSolicitacao = today;
        solicitacao.horarioSaida = today;
        solicitacao.horarioRetorno = today;
      }

      this.updateForm(solicitacao);

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
    const solicitacao = this.createFromForm();
    if (solicitacao.id !== undefined) {
      this.subscribeToSaveResponse(this.solicitacaoService.update(solicitacao));
    } else {
      this.subscribeToSaveResponse(this.solicitacaoService.create(solicitacao));
    }
  }

  trackCategoriaVeiculoById(index: number, item: ICategoriaVeiculo): number {
    return item.id!;
  }

  trackVeiculoById(index: number, item: IVeiculo): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolicitacao>>): void {
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

  protected updateForm(solicitacao: ISolicitacao): void {
    this.editForm.patchValue({
      id: solicitacao.id,
      origem: solicitacao.origem,
      destino: solicitacao.destino,
      dataSolicitacao: solicitacao.dataSolicitacao ? solicitacao.dataSolicitacao.format(DATE_TIME_FORMAT) : null,
      horarioSaida: solicitacao.horarioSaida ? solicitacao.horarioSaida.format(DATE_TIME_FORMAT) : null,
      horarioRetorno: solicitacao.horarioRetorno ? solicitacao.horarioRetorno.format(DATE_TIME_FORMAT) : null,
      distanciaEstimadaKm: solicitacao.distanciaEstimadaKm,
      justificativa: solicitacao.justificativa,
      status: solicitacao.status,
      nPessoas: solicitacao.nPessoas,
      peso: solicitacao.peso,
      categoria: solicitacao.categoria,
      veiculoAlocado: solicitacao.veiculoAlocado,
      solicitante: solicitacao.solicitante,
      autorizador: solicitacao.autorizador,
      motorista: solicitacao.motorista,
    });

    this.categoriaVeiculosSharedCollection = this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing(
      this.categoriaVeiculosSharedCollection,
      solicitacao.categoria
    );
    this.veiculosSharedCollection = this.veiculoService.addVeiculoToCollectionIfMissing(
      this.veiculosSharedCollection,
      solicitacao.veiculoAlocado
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      solicitacao.solicitante,
      solicitacao.autorizador,
      solicitacao.motorista
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaVeiculoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaVeiculo[]>) => res.body ?? []))
      .pipe(
        map((categoriaVeiculos: ICategoriaVeiculo[]) =>
          this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculos, this.editForm.get('categoria')!.value)
        )
      )
      .subscribe((categoriaVeiculos: ICategoriaVeiculo[]) => (this.categoriaVeiculosSharedCollection = categoriaVeiculos));

    this.veiculoService
      .query()
      .pipe(map((res: HttpResponse<IVeiculo[]>) => res.body ?? []))
      .pipe(
        map((veiculos: IVeiculo[]) =>
          this.veiculoService.addVeiculoToCollectionIfMissing(veiculos, this.editForm.get('veiculoAlocado')!.value)
        )
      )
      .subscribe((veiculos: IVeiculo[]) => (this.veiculosSharedCollection = veiculos));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(
            usuarios,
            this.editForm.get('solicitante')!.value,
            this.editForm.get('autorizador')!.value,
            this.editForm.get('motorista')!.value
          )
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): ISolicitacao {
    return {
      ...new Solicitacao(),
      id: this.editForm.get(['id'])!.value,
      origem: this.editForm.get(['origem'])!.value,
      destino: this.editForm.get(['destino'])!.value,
      dataSolicitacao: this.editForm.get(['dataSolicitacao'])!.value
        ? dayjs(this.editForm.get(['dataSolicitacao'])!.value, DATE_TIME_FORMAT)
        : undefined,
      horarioSaida: this.editForm.get(['horarioSaida'])!.value
        ? dayjs(this.editForm.get(['horarioSaida'])!.value, DATE_TIME_FORMAT)
        : undefined,
      horarioRetorno: this.editForm.get(['horarioRetorno'])!.value
        ? dayjs(this.editForm.get(['horarioRetorno'])!.value, DATE_TIME_FORMAT)
        : undefined,
      distanciaEstimadaKm: this.editForm.get(['distanciaEstimadaKm'])!.value,
      justificativa: this.editForm.get(['justificativa'])!.value,
      status: this.editForm.get(['status'])!.value,
      nPessoas: this.editForm.get(['nPessoas'])!.value,
      peso: this.editForm.get(['peso'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      veiculoAlocado: this.editForm.get(['veiculoAlocado'])!.value,
      solicitante: this.editForm.get(['solicitante'])!.value,
      autorizador: this.editForm.get(['autorizador'])!.value,
      motorista: this.editForm.get(['motorista'])!.value,
    };
  }
}
