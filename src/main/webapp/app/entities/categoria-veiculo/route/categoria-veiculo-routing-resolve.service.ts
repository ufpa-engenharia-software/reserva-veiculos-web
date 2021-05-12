import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaVeiculo, CategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

@Injectable({ providedIn: 'root' })
export class CategoriaVeiculoRoutingResolveService implements Resolve<ICategoriaVeiculo> {
  constructor(protected service: CategoriaVeiculoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaVeiculo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaVeiculo: HttpResponse<CategoriaVeiculo>) => {
          if (categoriaVeiculo.body) {
            return of(categoriaVeiculo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategoriaVeiculo());
  }
}
