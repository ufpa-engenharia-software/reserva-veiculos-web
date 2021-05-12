import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISolicitacao, getSolicitacaoIdentifier } from '../solicitacao.model';

export type EntityResponseType = HttpResponse<ISolicitacao>;
export type EntityArrayResponseType = HttpResponse<ISolicitacao[]>;

@Injectable({ providedIn: 'root' })
export class SolicitacaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/solicitacaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(solicitacao: ISolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .post<ISolicitacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(solicitacao: ISolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .put<ISolicitacao>(`${this.resourceUrl}/${getSolicitacaoIdentifier(solicitacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(solicitacao: ISolicitacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacao);
    return this.http
      .patch<ISolicitacao>(`${this.resourceUrl}/${getSolicitacaoIdentifier(solicitacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISolicitacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISolicitacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSolicitacaoToCollectionIfMissing(
    solicitacaoCollection: ISolicitacao[],
    ...solicitacaosToCheck: (ISolicitacao | null | undefined)[]
  ): ISolicitacao[] {
    const solicitacaos: ISolicitacao[] = solicitacaosToCheck.filter(isPresent);
    if (solicitacaos.length > 0) {
      const solicitacaoCollectionIdentifiers = solicitacaoCollection.map(solicitacaoItem => getSolicitacaoIdentifier(solicitacaoItem)!);
      const solicitacaosToAdd = solicitacaos.filter(solicitacaoItem => {
        const solicitacaoIdentifier = getSolicitacaoIdentifier(solicitacaoItem);
        if (solicitacaoIdentifier == null || solicitacaoCollectionIdentifiers.includes(solicitacaoIdentifier)) {
          return false;
        }
        solicitacaoCollectionIdentifiers.push(solicitacaoIdentifier);
        return true;
      });
      return [...solicitacaosToAdd, ...solicitacaoCollection];
    }
    return solicitacaoCollection;
  }

  protected convertDateFromClient(solicitacao: ISolicitacao): ISolicitacao {
    return Object.assign({}, solicitacao, {
      dataSolicitacao: solicitacao.dataSolicitacao?.isValid() ? solicitacao.dataSolicitacao.toJSON() : undefined,
      horarioSaida: solicitacao.horarioSaida?.isValid() ? solicitacao.horarioSaida.toJSON() : undefined,
      horarioRetorno: solicitacao.horarioRetorno?.isValid() ? solicitacao.horarioRetorno.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataSolicitacao = res.body.dataSolicitacao ? dayjs(res.body.dataSolicitacao) : undefined;
      res.body.horarioSaida = res.body.horarioSaida ? dayjs(res.body.horarioSaida) : undefined;
      res.body.horarioRetorno = res.body.horarioRetorno ? dayjs(res.body.horarioRetorno) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((solicitacao: ISolicitacao) => {
        solicitacao.dataSolicitacao = solicitacao.dataSolicitacao ? dayjs(solicitacao.dataSolicitacao) : undefined;
        solicitacao.horarioSaida = solicitacao.horarioSaida ? dayjs(solicitacao.horarioSaida) : undefined;
        solicitacao.horarioRetorno = solicitacao.horarioRetorno ? dayjs(solicitacao.horarioRetorno) : undefined;
      });
    }
    return res;
  }
}
