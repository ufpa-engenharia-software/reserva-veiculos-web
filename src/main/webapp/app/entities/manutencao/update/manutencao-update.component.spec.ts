jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ManutencaoService } from '../service/manutencao.service';
import { IManutencao, Manutencao } from '../manutencao.model';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';

import { ManutencaoUpdateComponent } from './manutencao-update.component';

describe('Component Tests', () => {
  describe('Manutencao Management Update Component', () => {
    let comp: ManutencaoUpdateComponent;
    let fixture: ComponentFixture<ManutencaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let manutencaoService: ManutencaoService;
    let veiculoService: VeiculoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ManutencaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ManutencaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManutencaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      manutencaoService = TestBed.inject(ManutencaoService);
      veiculoService = TestBed.inject(VeiculoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Veiculo query and add missing value', () => {
        const manutencao: IManutencao = { id: 456 };
        const veiculo: IVeiculo = { id: 17502 };
        manutencao.veiculo = veiculo;

        const veiculoCollection: IVeiculo[] = [{ id: 92473 }];
        spyOn(veiculoService, 'query').and.returnValue(of(new HttpResponse({ body: veiculoCollection })));
        const additionalVeiculos = [veiculo];
        const expectedCollection: IVeiculo[] = [...additionalVeiculos, ...veiculoCollection];
        spyOn(veiculoService, 'addVeiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ manutencao });
        comp.ngOnInit();

        expect(veiculoService.query).toHaveBeenCalled();
        expect(veiculoService.addVeiculoToCollectionIfMissing).toHaveBeenCalledWith(veiculoCollection, ...additionalVeiculos);
        expect(comp.veiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const manutencao: IManutencao = { id: 456 };
        const veiculo: IVeiculo = { id: 5934 };
        manutencao.veiculo = veiculo;

        activatedRoute.data = of({ manutencao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(manutencao));
        expect(comp.veiculosSharedCollection).toContain(veiculo);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manutencao = { id: 123 };
        spyOn(manutencaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manutencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: manutencao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(manutencaoService.update).toHaveBeenCalledWith(manutencao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manutencao = new Manutencao();
        spyOn(manutencaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manutencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: manutencao }));
        saveSubject.complete();

        // THEN
        expect(manutencaoService.create).toHaveBeenCalledWith(manutencao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manutencao = { id: 123 };
        spyOn(manutencaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manutencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(manutencaoService.update).toHaveBeenCalledWith(manutencao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVeiculoById', () => {
        it('Should return tracked Veiculo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVeiculoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
