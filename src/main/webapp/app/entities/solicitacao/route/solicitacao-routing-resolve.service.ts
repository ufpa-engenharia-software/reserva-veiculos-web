import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISolicitacao, Solicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';

@Injectable({ providedIn: 'root' })
export class SolicitacaoRoutingResolveService implements Resolve<ISolicitacao> {
  constructor(protected service: SolicitacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISolicitacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((solicitacao: HttpResponse<Solicitacao>) => {
          if (solicitacao.body) {
            return of(solicitacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Solicitacao());
  }
}
