import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVeiculo, Veiculo } from '../veiculo.model';

import { VeiculoService } from './veiculo.service';

describe('Service Tests', () => {
  describe('Veiculo Service', () => {
    let service: VeiculoService;
    let httpMock: HttpTestingController;
    let elemDefault: IVeiculo;
    let expectedResult: IVeiculo | IVeiculo[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VeiculoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        placa: 'AAAAAAA',
        modelo: 'AAAAAAA',
        ano: 0,
        disponivel: false,
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

      it('should create a Veiculo', () => {
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

        service.create(new Veiculo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Veiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            placa: 'BBBBBB',
            modelo: 'BBBBBB',
            ano: 1,
            disponivel: true,
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

      it('should partial update a Veiculo', () => {
        const patchObject = Object.assign(
          {
            disponivel: true,
          },
          new Veiculo()
        );

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

      it('should return a list of Veiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            placa: 'BBBBBB',
            modelo: 'BBBBBB',
            ano: 1,
            disponivel: true,
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

      it('should delete a Veiculo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVeiculoToCollectionIfMissing', () => {
        it('should add a Veiculo to an empty array', () => {
          const veiculo: IVeiculo = { id: 123 };
          expectedResult = service.addVeiculoToCollectionIfMissing([], veiculo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(veiculo);
        });

        it('should not add a Veiculo to an array that contains it', () => {
          const veiculo: IVeiculo = { id: 123 };
          const veiculoCollection: IVeiculo[] = [
            {
              ...veiculo,
            },
            { id: 456 },
          ];
          expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, veiculo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Veiculo to an array that doesn't contain it", () => {
          const veiculo: IVeiculo = { id: 123 };
          const veiculoCollection: IVeiculo[] = [{ id: 456 }];
          expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, veiculo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(veiculo);
        });

        it('should add only unique Veiculo to an array', () => {
          const veiculoArray: IVeiculo[] = [{ id: 123 }, { id: 456 }, { id: 66036 }];
          const veiculoCollection: IVeiculo[] = [{ id: 123 }];
          expectedResult = service.addVeiculoToCollectionIfMissing(veiculoCollection, ...veiculoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const veiculo: IVeiculo = { id: 123 };
          const veiculo2: IVeiculo = { id: 456 };
          expectedResult = service.addVeiculoToCollectionIfMissing([], veiculo, veiculo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(veiculo);
          expect(expectedResult).toContain(veiculo2);
        });

        it('should accept null and undefined values', () => {
          const veiculo: IVeiculo = { id: 123 };
          expectedResult = service.addVeiculoToCollectionIfMissing([], null, veiculo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(veiculo);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
