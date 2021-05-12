import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFabricante, Fabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';

@Injectable({ providedIn: 'root' })
export class FabricanteRoutingResolveService implements Resolve<IFabricante> {
  constructor(protected service: FabricanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFabricante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fabricante: HttpResponse<Fabricante>) => {
          if (fabricante.body) {
            return of(fabricante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fabricante());
  }
}
