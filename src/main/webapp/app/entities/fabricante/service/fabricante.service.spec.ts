import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFabricante, Fabricante } from '../fabricante.model';

import { FabricanteService } from './fabricante.service';

describe('Service Tests', () => {
  describe('Fabricante Service', () => {
    let service: FabricanteService;
    let httpMock: HttpTestingController;
    let elemDefault: IFabricante;
    let expectedResult: IFabricante | IFabricante[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FabricanteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        criado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fabricante', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.create(new Fabricante()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fabricante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fabricante', () => {
        const patchObject = Object.assign({}, new Fabricante());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fabricante', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            criado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fabricante', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFabricanteToCollectionIfMissing', () => {
        it('should add a Fabricante to an empty array', () => {
          const fabricante: IFabricante = { id: 123 };
          expectedResult = service.addFabricanteToCollectionIfMissing([], fabricante);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fabricante);
        });

        it('should not add a Fabricante to an array that contains it', () => {
          const fabricante: IFabricante = { id: 123 };
          const fabricanteCollection: IFabricante[] = [
            {
              ...fabricante,
            },
            { id: 456 },
          ];
          expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, fabricante);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fabricante to an array that doesn't contain it", () => {
          const fabricante: IFabricante = { id: 123 };
          const fabricanteCollection: IFabricante[] = [{ id: 456 }];
          expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, fabricante);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fabricante);
        });

        it('should add only unique Fabricante to an array', () => {
          const fabricanteArray: IFabricante[] = [{ id: 123 }, { id: 456 }, { id: 52718 }];
          const fabricanteCollection: IFabricante[] = [{ id: 123 }];
          expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, ...fabricanteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fabricante: IFabricante = { id: 123 };
          const fabricante2: IFabricante = { id: 456 };
          expectedResult = service.addFabricanteToCollectionIfMissing([], fabricante, fabricante2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fabricante);
          expect(expectedResult).toContain(fabricante2);
        });

        it('should accept null and undefined values', () => {
          const fabricante: IFabricante = { id: 123 };
          expectedResult = service.addFabricanteToCollectionIfMissing([], null, fabricante, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fabricante);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
