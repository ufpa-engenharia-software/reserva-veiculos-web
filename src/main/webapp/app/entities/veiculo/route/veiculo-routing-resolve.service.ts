import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVeiculo, Veiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';

@Injectable({ providedIn: 'root' })
export class VeiculoRoutingResolveService implements Resolve<IVeiculo> {
  constructor(protected service: VeiculoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVeiculo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((veiculo: HttpResponse<Veiculo>) => {
          if (veiculo.body) {
            return of(veiculo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Veiculo());
  }
}
