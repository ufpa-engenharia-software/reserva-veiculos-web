import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVeiculo, getVeiculoIdentifier } from '../veiculo.model';

export type EntityResponseType = HttpResponse<IVeiculo>;
export type EntityArrayResponseType = HttpResponse<IVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/veiculos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(veiculo: IVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .post<IVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(veiculo: IVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .put<IVeiculo>(`${this.resourceUrl}/${getVeiculoIdentifier(veiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(veiculo: IVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(veiculo);
    return this.http
      .patch<IVeiculo>(`${this.resourceUrl}/${getVeiculoIdentifier(veiculo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVeiculoToCollectionIfMissing(veiculoCollection: IVeiculo[], ...veiculosToCheck: (IVeiculo | null | undefined)[]): IVeiculo[] {
    const veiculos: IVeiculo[] = veiculosToCheck.filter(isPresent);
    if (veiculos.length > 0) {
      const veiculoCollectionIdentifiers = veiculoCollection.map(veiculoItem => getVeiculoIdentifier(veiculoItem)!);
      const veiculosToAdd = veiculos.filter(veiculoItem => {
        const veiculoIdentifier = getVeiculoIdentifier(veiculoItem);
        if (veiculoIdentifier == null || veiculoCollectionIdentifiers.includes(veiculoIdentifier)) {
          return false;
        }
        veiculoCollectionIdentifiers.push(veiculoIdentifier);
        return true;
      });
      return [...veiculosToAdd, ...veiculoCollection];
    }
    return veiculoCollection;
  }

  protected convertDateFromClient(veiculo: IVeiculo): IVeiculo {
    return Object.assign({}, veiculo, {
      criado: veiculo.criado?.isValid() ? veiculo.criado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.criado = res.body.criado ? dayjs(res.body.criado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((veiculo: IVeiculo) => {
        veiculo.criado = veiculo.criado ? dayjs(veiculo.criado) : undefined;
      });
    }
    return res;
  }
}
