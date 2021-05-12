jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IManutencao, Manutencao } from '../manutencao.model';
import { ManutencaoService } from '../service/manutencao.service';

import { ManutencaoRoutingResolveService } from './manutencao-routing-resolve.service';

describe('Service Tests', () => {
  describe('Manutencao routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ManutencaoRoutingResolveService;
    let service: ManutencaoService;
    let resultManutencao: IManutencao | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ManutencaoRoutingResolveService);
      service = TestBed.inject(ManutencaoService);
      resultManutencao = undefined;
    });

    describe('resolve', () => {
      it('should return IManutencao returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManutencao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultManutencao).toEqual({ id: 123 });
      });

      it('should return new IManutencao if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManutencao = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultManutencao).toEqual(new Manutencao());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManutencao = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultManutencao).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
