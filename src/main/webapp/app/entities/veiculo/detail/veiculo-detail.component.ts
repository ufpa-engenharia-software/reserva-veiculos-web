import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVeiculo } from '../veiculo.model';

@Component({
  selector: 'jhi-veiculo-detail',
  templateUrl: './veiculo-detail.component.html',
})
export class VeiculoDetailComponent implements OnInit {
  veiculo: IVeiculo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ veiculo }) => {
      this.veiculo = veiculo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
