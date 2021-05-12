jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AvaliacaoSolicitacaoService } from '../service/avaliacao-solicitacao.service';
import { IAvaliacaoSolicitacao, AvaliacaoSolicitacao } from '../avaliacao-solicitacao.model';
import { ISolicitacao } from 'app/entities/solicitacao/solicitacao.model';
import { SolicitacaoService } from 'app/entities/solicitacao/service/solicitacao.service';

import { AvaliacaoSolicitacaoUpdateComponent } from './avaliacao-solicitacao-update.component';

describe('Component Tests', () => {
  describe('AvaliacaoSolicitacao Management Update Component', () => {
    let comp: AvaliacaoSolicitacaoUpdateComponent;
    let fixture: ComponentFixture<AvaliacaoSolicitacaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let avaliacaoSolicitacaoService: AvaliacaoSolicitacaoService;
    let solicitacaoService: SolicitacaoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AvaliacaoSolicitacaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AvaliacaoSolicitacaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AvaliacaoSolicitacaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      avaliacaoSolicitacaoService = TestBed.inject(AvaliacaoSolicitacaoService);
      solicitacaoService = TestBed.inject(SolicitacaoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call solicitacao query and add missing value', () => {
        const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 456 };
        const solicitacao: ISolicitacao = { id: 84888 };
        avaliacaoSolicitacao.solicitacao = solicitacao;

        const solicitacaoCollection: ISolicitacao[] = [{ id: 35102 }];
        spyOn(solicitacaoService, 'query').and.returnValue(of(new HttpResponse({ body: solicitacaoCollection })));
        const expectedCollection: ISolicitacao[] = [solicitacao, ...solicitacaoCollection];
        spyOn(solicitacaoService, 'addSolicitacaoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ avaliacaoSolicitacao });
        comp.ngOnInit();

        expect(solicitacaoService.query).toHaveBeenCalled();
        expect(solicitacaoService.addSolicitacaoToCollectionIfMissing).toHaveBeenCalledWith(solicitacaoCollection, solicitacao);
        expect(comp.solicitacaosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const avaliacaoSolicitacao: IAvaliacaoSolicitacao = { id: 456 };
        const solicitacao: ISolicitacao = { id: 4893 };
        avaliacaoSolicitacao.solicitacao = solicitacao;

        activatedRoute.data = of({ avaliacaoSolicitacao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(avaliacaoSolicitacao));
        expect(comp.solicitacaosCollection).toContain(solicitacao);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const avaliacaoSolicitacao = { id: 123 };
        spyOn(avaliacaoSolicitacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ avaliacaoSolicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: avaliacaoSolicitacao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(avaliacaoSolicitacaoService.update).toHaveBeenCalledWith(avaliacaoSolicitacao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const avaliacaoSolicitacao = new AvaliacaoSolicitacao();
        spyOn(avaliacaoSolicitacaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ avaliacaoSolicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: avaliacaoSolicitacao }));
        saveSubject.complete();

        // THEN
        expect(avaliacaoSolicitacaoService.create).toHaveBeenCalledWith(avaliacaoSolicitacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const avaliacaoSolicitacao = { id: 123 };
        spyOn(avaliacaoSolicitacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ avaliacaoSolicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(avaliacaoSolicitacaoService.update).toHaveBeenCalledWith(avaliacaoSolicitacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSolicitacaoById', () => {
        it('Should return tracked Solicitacao primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSolicitacaoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
