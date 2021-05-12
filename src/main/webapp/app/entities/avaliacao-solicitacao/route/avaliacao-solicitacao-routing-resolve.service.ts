import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvaliacaoSolicitacao, AvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';
import { AvaliacaoSolicitacaoService } from '../service/avaliacao-solicitacao.service';

@Injectable({ providedIn: 'root' })
export class AvaliacaoSolicitacaoRoutingResolveService implements Resolve<IAvaliacaoSolicitacao> {
  constructor(protected service: AvaliacaoSolicitacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAvaliacaoSolicitacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((avaliacaoSolicitacao: HttpResponse<AvaliacaoSolicitacao>) => {
          if (avaliacaoSolicitacao.body) {
            return of(avaliacaoSolicitacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AvaliacaoSolicitacao());
  }
}
