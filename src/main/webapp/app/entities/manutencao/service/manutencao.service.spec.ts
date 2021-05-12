import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IManutencao, Manutencao } from '../manutencao.model';

import { ManutencaoService } from './manutencao.service';

describe('Service Tests', () => {
  describe('Manutencao Service', () => {
    let service: ManutencaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IManutencao;
    let expectedResult: IManutencao | IManutencao[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ManutencaoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        km: 0,
        descricao: 'AAAAAAA',
        custo: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Manutencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Manutencao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Manutencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            km: 1,
            descricao: 'BBBBBB',
            custo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Manutencao', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
          },
          new Manutencao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Manutencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            km: 1,
            descricao: 'BBBBBB',
            custo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Manutencao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addManutencaoToCollectionIfMissing', () => {
        it('should add a Manutencao to an empty array', () => {
          const manutencao: IManutencao = { id: 123 };
          expectedResult = service.addManutencaoToCollectionIfMissing([], manutencao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(manutencao);
        });

        it('should not add a Manutencao to an array that contains it', () => {
          const manutencao: IManutencao = { id: 123 };
          const manutencaoCollection: IManutencao[] = [
            {
              ...manutencao,
            },
            { id: 456 },
          ];
          expectedResult = service.addManutencaoToCollectionIfMissing(manutencaoCollection, manutencao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Manutencao to an array that doesn't contain it", () => {
          const manutencao: IManutencao = { id: 123 };
          const manutencaoCollection: IManutencao[] = [{ id: 456 }];
          expectedResult = service.addManutencaoToCollectionIfMissing(manutencaoCollection, manutencao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(manutencao);
        });

        it('should add only unique Manutencao to an array', () => {
          const manutencaoArray: IManutencao[] = [{ id: 123 }, { id: 456 }, { id: 18882 }];
          const manutencaoCollection: IManutencao[] = [{ id: 123 }];
          expectedResult = service.addManutencaoToCollectionIfMissing(manutencaoCollection, ...manutencaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const manutencao: IManutencao = { id: 123 };
          const manutencao2: IManutencao = { id: 456 };
          expectedResult = service.addManutencaoToCollectionIfMissing([], manutencao, manutencao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(manutencao);
          expect(expectedResult).toContain(manutencao2);
        });

        it('should accept null and undefined values', () => {
          const manutencao: IManutencao = { id: 123 };
          expectedResult = service.addManutencaoToCollectionIfMissing([], null, manutencao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(manutencao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
