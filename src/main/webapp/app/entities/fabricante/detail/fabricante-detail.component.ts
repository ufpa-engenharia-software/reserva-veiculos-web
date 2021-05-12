import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFabricante } from '../fabricante.model';

@Component({
  selector: 'jhi-fabricante-detail',
  templateUrl: './fabricante-detail.component.html',
})
export class FabricanteDetailComponent implements OnInit {
  fabricante: IFabricante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricante }) => {
      this.fabricante = fabricante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
