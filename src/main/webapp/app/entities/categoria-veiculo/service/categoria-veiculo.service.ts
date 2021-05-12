import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaVeiculo, getCategoriaVeiculoIdentifier } from '../categoria-veiculo.model';

export type EntityResponseType = HttpResponse<ICategoriaVeiculo>;
export type EntityArrayResponseType = HttpResponse<ICategoriaVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaVeiculoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-veiculos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(categoriaVeiculo: ICategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.post<ICategoriaVeiculo>(this.resourceUrl, categoriaVeiculo, { observe: 'response' });
  }

  update(categoriaVeiculo: ICategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.put<ICategoriaVeiculo>(
      `${this.resourceUrl}/${getCategoriaVeiculoIdentifier(categoriaVeiculo) as number}`,
      categoriaVeiculo,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaVeiculo: ICategoriaVeiculo): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaVeiculo>(
      `${this.resourceUrl}/${getCategoriaVeiculoIdentifier(categoriaVeiculo) as number}`,
      categoriaVeiculo,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategoriaVeiculoToCollectionIfMissing(
    categoriaVeiculoCollection: ICategoriaVeiculo[],
    ...categoriaVeiculosToCheck: (ICategoriaVeiculo | null | undefined)[]
  ): ICategoriaVeiculo[] {
    const categoriaVeiculos: ICategoriaVeiculo[] = categoriaVeiculosToCheck.filter(isPresent);
    if (categoriaVeiculos.length > 0) {
      const categoriaVeiculoCollectionIdentifiers = categoriaVeiculoCollection.map(
        categoriaVeiculoItem => getCategoriaVeiculoIdentifier(categoriaVeiculoItem)!
      );
      const categoriaVeiculosToAdd = categoriaVeiculos.filter(categoriaVeiculoItem => {
        const categoriaVeiculoIdentifier = getCategoriaVeiculoIdentifier(categoriaVeiculoItem);
        if (categoriaVeiculoIdentifier == null || categoriaVeiculoCollectionIdentifiers.includes(categoriaVeiculoIdentifier)) {
          return false;
        }
        categoriaVeiculoCollectionIdentifiers.push(categoriaVeiculoIdentifier);
        return true;
      });
      return [...categoriaVeiculosToAdd, ...categoriaVeiculoCollection];
    }
    return categoriaVeiculoCollection;
  }
}
