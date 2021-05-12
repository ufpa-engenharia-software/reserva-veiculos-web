import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFabricante, getFabricanteIdentifier } from '../fabricante.model';

export type EntityResponseType = HttpResponse<IFabricante>;
export type EntityArrayResponseType = HttpResponse<IFabricante[]>;

@Injectable({ providedIn: 'root' })
export class FabricanteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fabricantes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fabricante: IFabricante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fabricante);
    return this.http
      .post<IFabricante>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fabricante: IFabricante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fabricante);
    return this.http
      .put<IFabricante>(`${this.resourceUrl}/${getFabricanteIdentifier(fabricante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fabricante: IFabricante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fabricante);
    return this.http
      .patch<IFabricante>(`${this.resourceUrl}/${getFabricanteIdentifier(fabricante) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFabricante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFabricante[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFabricanteToCollectionIfMissing(
    fabricanteCollection: IFabricante[],
    ...fabricantesToCheck: (IFabricante | null | undefined)[]
  ): IFabricante[] {
    const fabricantes: IFabricante[] = fabricantesToCheck.filter(isPresent);
    if (fabricantes.length > 0) {
      const fabricanteCollectionIdentifiers = fabricanteCollection.map(fabricanteItem => getFabricanteIdentifier(fabricanteItem)!);
      const fabricantesToAdd = fabricantes.filter(fabricanteItem => {
        const fabricanteIdentifier = getFabricanteIdentifier(fabricanteItem);
        if (fabricanteIdentifier == null || fabricanteCollectionIdentifiers.includes(fabricanteIdentifier)) {
          return false;
        }
        fabricanteCollectionIdentifiers.push(fabricanteIdentifier);
        return true;
      });
      return [...fabricantesToAdd, ...fabricanteCollection];
    }
    return fabricanteCollection;
  }

  protected convertDateFromClient(fabricante: IFabricante): IFabricante {
    return Object.assign({}, fabricante, {
      criado: fabricante.criado?.isValid() ? fabricante.criado.toJSON() : undefined,
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
      res.body.forEach((fabricante: IFabricante) => {
        fabricante.criado = fabricante.criado ? dayjs(fabricante.criado) : undefined;
      });
    }
    return res;
  }
}
