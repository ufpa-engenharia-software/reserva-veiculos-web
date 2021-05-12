import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICategoriaVeiculo, CategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

@Component({
  selector: 'jhi-categoria-veiculo-update',
  templateUrl: './categoria-veiculo-update.component.html',
})
export class CategoriaVeiculoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    capacidadePessoas: [],
    capacidadePeso: [],
    capacidadeArea: [],
    eixos: [],
    altura: [],
    nivelCNH: [],
  });

  constructor(
    protected categoriaVeiculoService: CategoriaVeiculoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaVeiculo }) => {
      this.updateForm(categoriaVeiculo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaVeiculo = this.createFromForm();
    if (categoriaVeiculo.id !== undefined) {
      this.subscribeToSaveResponse(this.categoriaVeiculoService.update(categoriaVeiculo));
    } else {
      this.subscribeToSaveResponse(this.categoriaVeiculoService.create(categoriaVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaVeiculo>>): void {
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

  protected updateForm(categoriaVeiculo: ICategoriaVeiculo): void {
    this.editForm.patchValue({
      id: categoriaVeiculo.id,
      nome: categoriaVeiculo.nome,
      capacidadePessoas: categoriaVeiculo.capacidadePessoas,
      capacidadePeso: categoriaVeiculo.capacidadePeso,
      capacidadeArea: categoriaVeiculo.capacidadeArea,
      eixos: categoriaVeiculo.eixos,
      altura: categoriaVeiculo.altura,
      nivelCNH: categoriaVeiculo.nivelCNH,
    });
  }

  protected createFromForm(): ICategoriaVeiculo {
    return {
      ...new CategoriaVeiculo(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      capacidadePessoas: this.editForm.get(['capacidadePessoas'])!.value,
      capacidadePeso: this.editForm.get(['capacidadePeso'])!.value,
      capacidadeArea: this.editForm.get(['capacidadeArea'])!.value,
      eixos: this.editForm.get(['eixos'])!.value,
      altura: this.editForm.get(['altura'])!.value,
      nivelCNH: this.editForm.get(['nivelCNH'])!.value,
    };
  }
}
