import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaVeiculo } from '../categoria-veiculo.model';

@Component({
  selector: 'jhi-categoria-veiculo-detail',
  templateUrl: './categoria-veiculo-detail.component.html',
})
export class CategoriaVeiculoDetailComponent implements OnInit {
  categoriaVeiculo: ICategoriaVeiculo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaVeiculo }) => {
      this.categoriaVeiculo = categoriaVeiculo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
