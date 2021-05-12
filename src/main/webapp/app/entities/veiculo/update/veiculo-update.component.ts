import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVeiculo, Veiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { FabricanteService } from 'app/entities/fabricante/service/fabricante.service';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-veiculo-update',
  templateUrl: './veiculo-update.component.html',
})
export class VeiculoUpdateComponent implements OnInit {
  isSaving = false;

  fabricantesSharedCollection: IFabricante[] = [];
  categoriaVeiculosSharedCollection: ICategoriaVeiculo[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    placa: [null, [Validators.required]],
    modelo: [],
    ano: [],
    disponivel: [],
    criado: [],
    fabricante: [],
    categoria: [],
    motoristasHabilitados: [],
  });

  constructor(
    protected veiculoService: VeiculoService,
    protected fabricanteService: FabricanteService,
    protected categoriaVeiculoService: CategoriaVeiculoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ veiculo }) => {
      if (veiculo.id === undefined) {
        const today = dayjs().startOf('day');
        veiculo.criado = today;
      }

      this.updateForm(veiculo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const veiculo = this.createFromForm();
    if (veiculo.id !== undefined) {
      this.subscribeToSaveResponse(this.veiculoService.update(veiculo));
    } else {
      this.subscribeToSaveResponse(this.veiculoService.create(veiculo));
    }
  }

  trackFabricanteById(index: number, item: IFabricante): number {
    return item.id!;
  }

  trackCategoriaVeiculoById(index: number, item: ICategoriaVeiculo): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  getSelectedUsuario(option: IUsuario, selectedVals?: IUsuario[]): IUsuario {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVeiculo>>): void {
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

  protected updateForm(veiculo: IVeiculo): void {
    this.editForm.patchValue({
      id: veiculo.id,
      placa: veiculo.placa,
      modelo: veiculo.modelo,
      ano: veiculo.ano,
      disponivel: veiculo.disponivel,
      criado: veiculo.criado ? veiculo.criado.format(DATE_TIME_FORMAT) : null,
      fabricante: veiculo.fabricante,
      categoria: veiculo.categoria,
      motoristasHabilitados: veiculo.motoristasHabilitados,
    });

    this.fabricantesSharedCollection = this.fabricanteService.addFabricanteToCollectionIfMissing(
      this.fabricantesSharedCollection,
      veiculo.fabricante
    );
    this.categoriaVeiculosSharedCollection = this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing(
      this.categoriaVeiculosSharedCollection,
      veiculo.categoria
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      ...(veiculo.motoristasHabilitados ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fabricanteService
      .query()
      .pipe(map((res: HttpResponse<IFabricante[]>) => res.body ?? []))
      .pipe(
        map((fabricantes: IFabricante[]) =>
          this.fabricanteService.addFabricanteToCollectionIfMissing(fabricantes, this.editForm.get('fabricante')!.value)
        )
      )
      .subscribe((fabricantes: IFabricante[]) => (this.fabricantesSharedCollection = fabricantes));

    this.categoriaVeiculoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaVeiculo[]>) => res.body ?? []))
      .pipe(
        map((categoriaVeiculos: ICategoriaVeiculo[]) =>
          this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculos, this.editForm.get('categoria')!.value)
        )
      )
      .subscribe((categoriaVeiculos: ICategoriaVeiculo[]) => (this.categoriaVeiculosSharedCollection = categoriaVeiculos));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, ...(this.editForm.get('motoristasHabilitados')!.value ?? []))
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IVeiculo {
    return {
      ...new Veiculo(),
      id: this.editForm.get(['id'])!.value,
      placa: this.editForm.get(['placa'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      ano: this.editForm.get(['ano'])!.value,
      disponivel: this.editForm.get(['disponivel'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      fabricante: this.editForm.get(['fabricante'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      motoristasHabilitados: this.editForm.get(['motoristasHabilitados'])!.value,
    };
  }
}
