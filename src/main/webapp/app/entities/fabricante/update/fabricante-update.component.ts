import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFabricante, Fabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';

@Component({
  selector: 'jhi-fabricante-update',
  templateUrl: './fabricante-update.component.html',
})
export class FabricanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    criado: [],
  });

  constructor(protected fabricanteService: FabricanteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricante }) => {
      if (fabricante.id === undefined) {
        const today = dayjs().startOf('day');
        fabricante.criado = today;
      }

      this.updateForm(fabricante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fabricante = this.createFromForm();
    if (fabricante.id !== undefined) {
      this.subscribeToSaveResponse(this.fabricanteService.update(fabricante));
    } else {
      this.subscribeToSaveResponse(this.fabricanteService.create(fabricante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFabricante>>): void {
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

  protected updateForm(fabricante: IFabricante): void {
    this.editForm.patchValue({
      id: fabricante.id,
      nome: fabricante.nome,
      criado: fabricante.criado ? fabricante.criado.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IFabricante {
    return {
      ...new Fabricante(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
