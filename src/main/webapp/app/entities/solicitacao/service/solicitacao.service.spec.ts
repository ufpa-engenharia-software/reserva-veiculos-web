import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { ISolicitacao, Solicitacao } from '../solicitacao.model';

import { SolicitacaoService } from './solicitacao.service';

describe('Service Tests', () => {
  describe('Solicitacao Service', () => {
    let service: SolicitacaoService;
    let httpMock: HttpTestingController;
    let elemDefault: ISolicitacao;
    let expectedResult: ISolicitacao | ISolicitacao[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SolicitacaoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        origem: 'AAAAAAA',
        destino: 'AAAAAAA',
        dataSolicitacao: currentDate,
        horarioSaida: currentDate,
        horarioRetorno: currentDate,
        distanciaEstimadaKm: 0,
        justificativa: 'AAAAAAA',
        status: Status.PENDENTE,
        nPessoas: 0,
        peso: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataSolicitacao: currentDate.format(DATE_TIME_FORMAT),
            horarioSaida: currentDate.format(DATE_TIME_FORMAT),
            horarioRetorno: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Solicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataSolicitacao: currentDate.format(DATE_TIME_FORMAT),
            horarioSaida: currentDate.format(DATE_TIME_FORMAT),
            horarioRetorno: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataSolicitacao: currentDate,
            horarioSaida: currentDate,
            horarioRetorno: currentDate,
          },
          returnedFromService
        );

        service.create(new Solicitacao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Solicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            origem: 'BBBBBB',
            destino: 'BBBBBB',
            dataSolicitacao: currentDate.format(DATE_TIME_FORMAT),
            horarioSaida: currentDate.format(DATE_TIME_FORMAT),
            horarioRetorno: currentDate.format(DATE_TIME_FORMAT),
            distanciaEstimadaKm: 1,
            justificativa: 'BBBBBB',
            status: 'BBBBBB',
            nPessoas: 1,
            peso: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataSolicitacao: currentDate,
            horarioSaida: currentDate,
            horarioRetorno: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Solicitacao', () => {
        const patchObject = Object.assign(
          {
            destino: 'BBBBBB',
            dataSolicitacao: currentDate.format(DATE_TIME_FORMAT),
            horarioRetorno: currentDate.format(DATE_TIME_FORMAT),
            distanciaEstimadaKm: 1,
            status: 'BBBBBB',
            nPessoas: 1,
            peso: 1,
          },
          new Solicitacao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataSolicitacao: currentDate,
            horarioSaida: currentDate,
            horarioRetorno: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Solicitacao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            origem: 'BBBBBB',
            destino: 'BBBBBB',
            dataSolicitacao: currentDate.format(DATE_TIME_FORMAT),
            horarioSaida: currentDate.format(DATE_TIME_FORMAT),
            horarioRetorno: currentDate.format(DATE_TIME_FORMAT),
            distanciaEstimadaKm: 1,
            justificativa: 'BBBBBB',
            status: 'BBBBBB',
            nPessoas: 1,
            peso: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataSolicitacao: currentDate,
            horarioSaida: currentDate,
            horarioRetorno: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Solicitacao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSolicitacaoToCollectionIfMissing', () => {
        it('should add a Solicitacao to an empty array', () => {
          const solicitacao: ISolicitacao = { id: 123 };
          expectedResult = service.addSolicitacaoToCollectionIfMissing([], solicitacao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(solicitacao);
        });

        it('should not add a Solicitacao to an array that contains it', () => {
          const solicitacao: ISolicitacao = { id: 123 };
          const solicitacaoCollection: ISolicitacao[] = [
            {
              ...solicitacao,
            },
            { id: 456 },
          ];
          expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, solicitacao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Solicitacao to an array that doesn't contain it", () => {
          const solicitacao: ISolicitacao = { id: 123 };
          const solicitacaoCollection: ISolicitacao[] = [{ id: 456 }];
          expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, solicitacao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(solicitacao);
        });

        it('should add only unique Solicitacao to an array', () => {
          const solicitacaoArray: ISolicitacao[] = [{ id: 123 }, { id: 456 }, { id: 33997 }];
          const solicitacaoCollection: ISolicitacao[] = [{ id: 123 }];
          expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, ...solicitacaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const solicitacao: ISolicitacao = { id: 123 };
          const solicitacao2: ISolicitacao = { id: 456 };
          expectedResult = service.addSolicitacaoToCollectionIfMissing([], solicitacao, solicitacao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(solicitacao);
          expect(expectedResult).toContain(solicitacao2);
        });

        it('should accept null and undefined values', () => {
          const solicitacao: ISolicitacao = { id: 123 };
          expectedResult = service.addSolicitacaoToCollectionIfMissing([], null, solicitacao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(solicitacao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
