jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';
import { ICategoriaVeiculo, CategoriaVeiculo } from '../categoria-veiculo.model';

import { CategoriaVeiculoUpdateComponent } from './categoria-veiculo-update.component';

describe('Component Tests', () => {
  describe('CategoriaVeiculo Management Update Component', () => {
    let comp: CategoriaVeiculoUpdateComponent;
    let fixture: ComponentFixture<CategoriaVeiculoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let categoriaVeiculoService: CategoriaVeiculoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CategoriaVeiculoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CategoriaVeiculoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CategoriaVeiculoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      categoriaVeiculoService = TestBed.inject(CategoriaVeiculoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const categoriaVeiculo: ICategoriaVeiculo = { id: 456 };

        activatedRoute.data = of({ categoriaVeiculo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(categoriaVeiculo));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categoriaVeiculo = { id: 123 };
        spyOn(categoriaVeiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categoriaVeiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categoriaVeiculo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(categoriaVeiculoService.update).toHaveBeenCalledWith(categoriaVeiculo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categoriaVeiculo = new CategoriaVeiculo();
        spyOn(categoriaVeiculoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categoriaVeiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categoriaVeiculo }));
        saveSubject.complete();

        // THEN
        expect(categoriaVeiculoService.create).toHaveBeenCalledWith(categoriaVeiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categoriaVeiculo = { id: 123 };
        spyOn(categoriaVeiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categoriaVeiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(categoriaVeiculoService.update).toHaveBeenCalledWith(categoriaVeiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
