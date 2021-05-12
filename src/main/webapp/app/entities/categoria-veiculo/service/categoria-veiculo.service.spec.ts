import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NivelCNH } from 'app/entities/enumerations/nivel-cnh.model';
import { ICategoriaVeiculo, CategoriaVeiculo } from '../categoria-veiculo.model';

import { CategoriaVeiculoService } from './categoria-veiculo.service';

describe('Service Tests', () => {
  describe('CategoriaVeiculo Service', () => {
    let service: CategoriaVeiculoService;
    let httpMock: HttpTestingController;
    let elemDefault: ICategoriaVeiculo;
    let expectedResult: ICategoriaVeiculo | ICategoriaVeiculo[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CategoriaVeiculoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        capacidadePessoas: 0,
        capacidadePeso: 0,
        capacidadeArea: 0,
        eixos: 0,
        altura: 0,
        nivelCNH: NivelCNH.A,
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

      it('should create a CategoriaVeiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CategoriaVeiculo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CategoriaVeiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            capacidadePessoas: 1,
            capacidadePeso: 1,
            capacidadeArea: 1,
            eixos: 1,
            altura: 1,
            nivelCNH: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CategoriaVeiculo', () => {
        const patchObject = Object.assign(
          {
            capacidadePessoas: 1,
            capacidadePeso: 1,
            capacidadeArea: 1,
          },
          new CategoriaVeiculo()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CategoriaVeiculo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            capacidadePessoas: 1,
            capacidadePeso: 1,
            capacidadeArea: 1,
            eixos: 1,
            altura: 1,
            nivelCNH: 'BBBBBB',
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

      it('should delete a CategoriaVeiculo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCategoriaVeiculoToCollectionIfMissing', () => {
        it('should add a CategoriaVeiculo to an empty array', () => {
          const categoriaVeiculo: ICategoriaVeiculo = { id: 123 };
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], categoriaVeiculo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categoriaVeiculo);
        });

        it('should not add a CategoriaVeiculo to an array that contains it', () => {
          const categoriaVeiculo: ICategoriaVeiculo = { id: 123 };
          const categoriaVeiculoCollection: ICategoriaVeiculo[] = [
            {
              ...categoriaVeiculo,
            },
            { id: 456 },
          ];
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, categoriaVeiculo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CategoriaVeiculo to an array that doesn't contain it", () => {
          const categoriaVeiculo: ICategoriaVeiculo = { id: 123 };
          const categoriaVeiculoCollection: ICategoriaVeiculo[] = [{ id: 456 }];
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, categoriaVeiculo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categoriaVeiculo);
        });

        it('should add only unique CategoriaVeiculo to an array', () => {
          const categoriaVeiculoArray: ICategoriaVeiculo[] = [{ id: 123 }, { id: 456 }, { id: 16910 }];
          const categoriaVeiculoCollection: ICategoriaVeiculo[] = [{ id: 123 }];
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, ...categoriaVeiculoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const categoriaVeiculo: ICategoriaVeiculo = { id: 123 };
          const categoriaVeiculo2: ICategoriaVeiculo = { id: 456 };
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], categoriaVeiculo, categoriaVeiculo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categoriaVeiculo);
          expect(expectedResult).toContain(categoriaVeiculo2);
        });

        it('should accept null and undefined values', () => {
          const categoriaVeiculo: ICategoriaVeiculo = { id: 123 };
          expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], null, categoriaVeiculo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categoriaVeiculo);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
