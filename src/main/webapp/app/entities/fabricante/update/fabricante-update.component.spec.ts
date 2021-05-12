jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FabricanteService } from '../service/fabricante.service';
import { IFabricante, Fabricante } from '../fabricante.model';

import { FabricanteUpdateComponent } from './fabricante-update.component';

describe('Component Tests', () => {
  describe('Fabricante Management Update Component', () => {
    let comp: FabricanteUpdateComponent;
    let fixture: ComponentFixture<FabricanteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fabricanteService: FabricanteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FabricanteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FabricanteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FabricanteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fabricanteService = TestBed.inject(FabricanteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fabricante: IFabricante = { id: 456 };

        activatedRoute.data = of({ fabricante });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fabricante));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricante = { id: 123 };
        spyOn(fabricanteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fabricante }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fabricanteService.update).toHaveBeenCalledWith(fabricante);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricante = new Fabricante();
        spyOn(fabricanteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fabricante }));
        saveSubject.complete();

        // THEN
        expect(fabricanteService.create).toHaveBeenCalledWith(fabricante);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricante = { id: 123 };
        spyOn(fabricanteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricante });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fabricanteService.update).toHaveBeenCalledWith(fabricante);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
