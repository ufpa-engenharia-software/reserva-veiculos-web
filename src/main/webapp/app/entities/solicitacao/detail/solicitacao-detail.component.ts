import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISolicitacao } from '../solicitacao.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-solicitacao-detail',
  templateUrl: './solicitacao-detail.component.html',
})
export class SolicitacaoDetailComponent implements OnInit {
  solicitacao: ISolicitacao | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solicitacao }) => {
      this.solicitacao = solicitacao;
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
