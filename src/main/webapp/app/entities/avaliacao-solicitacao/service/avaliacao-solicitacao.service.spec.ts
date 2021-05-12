import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { IAvaliacaoSolicitacao, AvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';

import { AvaliacaoSolicitacaoService } from './avaliacao-solicitacao.service';

describe('Service Tests', () => {
  describe('AvaliacaoSolicitacao Service', () => {
    let service: AvaliacaoSolicitacaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IAvaliacaoSolicitacao;
    let expectedResult: IAvaliacaoSolicitacao | IAvaliacaoSolicitacao[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AvaliacaoSolicitacaoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        valorGasolina: 0,
        totalGasto: 0,
        statusSolicitacao: Status.PENDENTE,
        justificativaStatus: 'AAAAAAA',
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

      it('should create a AvaliacaoSolicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AvaliacaoSolicitacao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AvaliacaoSolicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            valorGasolina: 1,
            totalGasto: 1,
            statusSolicitacao: 'BBBBBB',
            justificativaStatus: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AvaliacaoSolicitacao', () => {
        const patchObject = Object.assign(
          {
            valorGasolina: 1,
            totalGasto: 1,
            statusSolicitacao: 'BBBBBB',
            justificativaStatus: 'BBBBBB',
          },
          new AvaliacaoSolicitacao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AvaliacaoSolicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            valorGasolina: 1,
            totalGasto: 1,
            statusSolicitacao: 'BBBBBB',
            justificativaStatus: 'BBBBBB',
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

      it('should delete a AvaliacaoSolicitacao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAvaliacaoSolicitacaoToCollectionIfMissing', () => {
        it('should add a AvaliacaoSolicitacao to an empty array', () => {
          const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 123 };
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing([], avaliacaoSolicitacao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(avaliacaoSolicitacao);
        });

        it('should not add a AvaliacaoSolicitacao to an array that contains it', () => {
          const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 123 };
          const avaliacaoSolicitacaoCollection: IAvaliacaoSolicitacao[] = [
            {
              ...avaliacaoSolicitacao,
            },
            { id: 456 },
          ];
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing(avaliacaoSolicitacaoCollection, avaliacaoSolicitacao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AvaliacaoSolicitacao to an array that doesn't contain it", () => {
          const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 123 };
          const avaliacaoSolicitacaoCollection: IAvaliacaoSolicitacao[] = [{ id: 456 }];
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing(avaliacaoSolicitacaoCollection, avaliacaoSolicitacao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(avaliacaoSolicitacao);
        });

        it('should add only unique AvaliacaoSolicitacao to an array', () => {
          const avaliacaoSolicitacaoArray: IAvaliacaoSolicitacao[] = [{ id: 123 }, { id: 456 }, { id: 38168 }];
          const avaliacaoSolicitacaoCollection: IAvaliacaoSolicitacao[] = [{ id: 123 }];
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing(
            avaliacaoSolicitacaoCollection,
            ...avaliacaoSolicitacaoArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 123 };
          const avaliacaoSolicitacao2: IAvaliacaoSolicitacao = { id: 456 };
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing([], avaliacaoSolicitacao, avaliacaoSolicitacao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(avaliacaoSolicitacao);
          expect(expectedResult).toContain(avaliacaoSolicitacao2);
        });

        it('should accept null and undefined values', () => {
          const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 123 };
          expectedResult = service.addAvaliacaoSolicitacaoToCollectionIfMissing([], null, avaliacaoSolicitacao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(avaliacaoSolicitacao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
