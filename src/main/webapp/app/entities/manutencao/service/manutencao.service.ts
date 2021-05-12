import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManutencao, getManutencaoIdentifier } from '../manutencao.model';

export type EntityResponseType = HttpResponse<IManutencao>;
export type EntityArrayResponseType = HttpResponse<IManutencao[]>;

@Injectable({ providedIn: 'root' })
export class ManutencaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/manutencaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(manutencao: IManutencao): Observable<EntityResponseType> {
    return this.http.post<IManutencao>(this.resourceUrl, manutencao, { observe: 'response' });
  }

  update(manutencao: IManutencao): Observable<EntityResponseType> {
    return this.http.put<IManutencao>(`${this.resourceUrl}/${getManutencaoIdentifier(manutencao) as number}`, manutencao, {
      observe: 'response',
    });
  }

  partialUpdate(manutencao: IManutencao): Observable<EntityResponseType> {
    return this.http.patch<IManutencao>(`${this.resourceUrl}/${getManutencaoIdentifier(manutencao) as number}`, manutencao, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManutencao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManutencao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addManutencaoToCollectionIfMissing(
    manutencaoCollection: IManutencao[],
    ...manutencaosToCheck: (IManutencao | null | undefined)[]
  ): IManutencao[] {
    const manutencaos: IManutencao[] = manutencaosToCheck.filter(isPresent);
    if (manutencaos.length > 0) {
      const manutencaoCollectionIdentifiers = manutencaoCollection.map(manutencaoItem => getManutencaoIdentifier(manutencaoItem)!);
      const manutencaosToAdd = manutencaos.filter(manutencaoItem => {
        const manutencaoIdentifier = getManutencaoIdentifier(manutencaoItem);
        if (manutencaoIdentifier == null || manutencaoCollectionIdentifiers.includes(manutencaoIdentifier)) {
          return false;
        }
        manutencaoCollectionIdentifiers.push(manutencaoIdentifier);
        return true;
      });
      return [...manutencaosToAdd, ...manutencaoCollection];
    }
    return manutencaoCollection;
  }
}
