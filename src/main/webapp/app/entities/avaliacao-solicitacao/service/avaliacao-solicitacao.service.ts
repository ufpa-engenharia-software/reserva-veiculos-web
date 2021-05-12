import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAvaliacaoSolicitacao, getAvaliacaoSolicitacaoIdentifier } from '../avaliacao-solicitacao.model';

export type EntityResponseType = HttpResponse<IAvaliacaoSolicitacao>;
export type EntityArrayResponseType = HttpResponse<IAvaliacaoSolicitacao[]>;

@Injectable({ providedIn: 'root' })
export class AvaliacaoSolicitacaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/avaliacao-solicitacaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(avaliacaoSolicitacao: IAvaliacaoSolicitacao): Observable<EntityResponseType> {
    return this.http.post<IAvaliacaoSolicitacao>(this.resourceUrl, avaliacaoSolicitacao, { observe: 'response' });
  }

  update(avaliacaoSolicitacao: IAvaliacaoSolicitacao): Observable<EntityResponseType> {
    return this.http.put<IAvaliacaoSolicitacao>(
      `${this.resourceUrl}/${getAvaliacaoSolicitacaoIdentifier(avaliacaoSolicitacao) as number}`,
      avaliacaoSolicitacao,
      { observe: 'response' }
    );
  }

  partialUpdate(avaliacaoSolicitacao: IAvaliacaoSolicitacao): Observable<EntityResponseType> {
    return this.http.patch<IAvaliacaoSolicitacao>(
      `${this.resourceUrl}/${getAvaliacaoSolicitacaoIdentifier(avaliacaoSolicitacao) as number}`,
      avaliacaoSolicitacao,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAvaliacaoSolicitacao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAvaliacaoSolicitacao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAvaliacaoSolicitacaoToCollectionIfMissing(
    avaliacaoSolicitacaoCollection: IAvaliacaoSolicitacao[],
    ...avaliacaoSolicitacaosToCheck: (IAvaliacaoSolicitacao | null | undefined)[]
  ): IAvaliacaoSolicitacao[] {
    const avaliacaoSolicitacaos: IAvaliacaoSolicitacao[] = avaliacaoSolicitacaosToCheck.filter(isPresent);
    if (avaliacaoSolicitacaos.length > 0) {
      const avaliacaoSolicitacaoCollectionIdentifiers = avaliacaoSolicitacaoCollection.map(
        avaliacaoSolicitacaoItem => getAvaliacaoSolicitacaoIdentifier(avaliacaoSolicitacaoItem)!
      );
      const avaliacaoSolicitacaosToAdd = avaliacaoSolicitacaos.filter(avaliacaoSolicitacaoItem => {
        const avaliacaoSolicitacaoIdentifier = getAvaliacaoSolicitacaoIdentifier(avaliacaoSolicitacaoItem);
        if (avaliacaoSolicitacaoIdentifier == null || avaliacaoSolicitacaoCollectionIdentifiers.includes(avaliacaoSolicitacaoIdentifier)) {
          return false;
        }
        avaliacaoSolicitacaoCollectionIdentifiers.push(avaliacaoSolicitacaoIdentifier);
        return true;
      });
      return [...avaliacaoSolicitacaosToAdd, ...avaliacaoSolicitacaoCollection];
    }
    return avaliacaoSolicitacaoCollection;
  }
}
