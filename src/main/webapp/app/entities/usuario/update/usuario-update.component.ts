import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    perfil: [],
    identificacao: [],
    nidentificao: [],
    cpf: [],
    email: [],
    celular: [],
    whatsapp: [],
    ativo: [],
    criado: [],
    nivelCNH: [],
  });

  constructor(protected usuarioService: UsuarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.criado = today;
      }

      this.updateForm(usuario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      nome: usuario.nome,
      perfil: usuario.perfil,
      identificacao: usuario.identificacao,
      nidentificao: usuario.nidentificao,
      cpf: usuario.cpf,
      email: usuario.email,
      celular: usuario.celular,
      whatsapp: usuario.whatsapp,
      ativo: usuario.ativo,
      criado: usuario.criado ? usuario.criado.format(DATE_TIME_FORMAT) : null,
      nivelCNH: usuario.nivelCNH,
    });
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      perfil: this.editForm.get(['perfil'])!.value,
      identificacao: this.editForm.get(['identificacao'])!.value,
      nidentificao: this.editForm.get(['nidentificao'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      email: this.editForm.get(['email'])!.value,
      celular: this.editForm.get(['celular'])!.value,
      whatsapp: this.editForm.get(['whatsapp'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      criado: this.editForm.get(['criado'])!.value ? dayjs(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      nivelCNH: this.editForm.get(['nivelCNH'])!.value,
    };
  }
}
