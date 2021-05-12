jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICategoriaVeiculo, CategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

import { CategoriaVeiculoRoutingResolveService } from './categoria-veiculo-routing-resolve.service';

describe('Service Tests', () => {
  describe('CategoriaVeiculo routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CategoriaVeiculoRoutingResolveService;
    let service: CategoriaVeiculoService;
    let resultCategoriaVeiculo: ICategoriaVeiculo | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CategoriaVeiculoRoutingResolveService);
      service = TestBed.inject(CategoriaVeiculoService);
      resultCategoriaVeiculo = undefined;
    });

    describe('resolve', () => {
      it('should return ICategoriaVeiculo returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategoriaVeiculo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCategoriaVeiculo).toEqual({ id: 123 });
      });

      it('should return new ICategoriaVeiculo if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategoriaVeiculo = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCategoriaVeiculo).toEqual(new CategoriaVeiculo());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategoriaVeiculo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCategoriaVeiculo).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
