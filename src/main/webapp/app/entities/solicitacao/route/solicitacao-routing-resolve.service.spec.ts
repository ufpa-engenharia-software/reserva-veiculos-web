jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISolicitacao, Solicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';

import { SolicitacaoRoutingResolveService } from './solicitacao-routing-resolve.service';

describe('Service Tests', () => {
  describe('Solicitacao routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SolicitacaoRoutingResolveService;
    let service: SolicitacaoService;
    let resultSolicitacao: ISolicitacao | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SolicitacaoRoutingResolveService);
      service = TestBed.inject(SolicitacaoService);
      resultSolicitacao = undefined;
    });

    describe('resolve', () => {
      it('should return ISolicitacao returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSolicitacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSolicitacao).toEqual({ id: 123 });
      });

      it('should return new ISolicitacao if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSolicitacao = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSolicitacao).toEqual(new Solicitacao());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSolicitacao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSolicitacao).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
