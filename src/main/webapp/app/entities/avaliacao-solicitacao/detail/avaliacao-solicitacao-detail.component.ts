import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-avaliacao-solicitacao-detail',
  templateUrl: './avaliacao-solicitacao-detail.component.html',
})
export class AvaliacaoSolicitacaoDetailComponent implements OnInit {
  avaliacaoSolicitacao: IAvaliacaoSolicitacao | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avaliacaoSolicitacao }) => {
      this.avaliacaoSolicitacao = avaliacaoSolicitacao;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
