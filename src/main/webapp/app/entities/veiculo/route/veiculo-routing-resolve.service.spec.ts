jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVeiculo, Veiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';

import { VeiculoRoutingResolveService } from './veiculo-routing-resolve.service';

describe('Service Tests', () => {
  describe('Veiculo routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VeiculoRoutingResolveService;
    let service: VeiculoService;
    let resultVeiculo: IVeiculo | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VeiculoRoutingResolveService);
      service = TestBed.inject(VeiculoService);
      resultVeiculo = undefined;
    });

    describe('resolve', () => {
      it('should return IVeiculo returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVeiculo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVeiculo).toEqual({ id: 123 });
      });

      it('should return new IVeiculo if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVeiculo = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVeiculo).toEqual(new Veiculo());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVeiculo = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVeiculo).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
