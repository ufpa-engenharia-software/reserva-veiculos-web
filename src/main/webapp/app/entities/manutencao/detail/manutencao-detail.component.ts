import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManutencao } from '../manutencao.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-manutencao-detail',
  templateUrl: './manutencao-detail.component.html',
})
export class ManutencaoDetailComponent implements OnInit {
  manutencao: IManutencao | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manutencao }) => {
      this.manutencao = manutencao;
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
