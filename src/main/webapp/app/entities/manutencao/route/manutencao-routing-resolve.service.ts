import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManutencao, Manutencao } from '../manutencao.model';
import { ManutencaoService } from '../service/manutencao.service';

@Injectable({ providedIn: 'root' })
export class ManutencaoRoutingResolveService implements Resolve<IManutencao> {
  constructor(protected service: ManutencaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManutencao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manutencao: HttpResponse<Manutencao>) => {
          if (manutencao.body) {
            return of(manutencao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Manutencao());
  }
}
