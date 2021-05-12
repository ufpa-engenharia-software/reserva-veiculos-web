jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VeiculoService } from '../service/veiculo.service';
import { IVeiculo, Veiculo } from '../veiculo.model';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { FabricanteService } from 'app/entities/fabricante/service/fabricante.service';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { VeiculoUpdateComponent } from './veiculo-update.component';

describe('Component Tests', () => {
  describe('Veiculo Management Update Component', () => {
    let comp: VeiculoUpdateComponent;
    let fixture: ComponentFixture<VeiculoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let veiculoService: VeiculoService;
    let fabricanteService: FabricanteService;
    let categoriaVeiculoService: CategoriaVeiculoService;
    let usuarioService: UsuarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VeiculoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VeiculoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VeiculoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      veiculoService = TestBed.inject(VeiculoService);
      fabricanteService = TestBed.inject(FabricanteService);
      categoriaVeiculoService = TestBed.inject(CategoriaVeiculoService);
      usuarioService = TestBed.inject(UsuarioService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Fabricante query and add missing value', () => {
        const veiculo: IVeiculo = { id: 456 };
        const fabricante: IFabricante = { id: 29299 };
        veiculo.fabricante = fabricante;

        const fabricanteCollection: IFabricante[] = [{ id: 20507 }];
        spyOn(fabricanteService, 'query').and.returnValue(of(new HttpResponse({ body: fabricanteCollection })));
        const additionalFabricantes = [fabricante];
        const expectedCollection: IFabricante[] = [...additionalFabricantes, ...fabricanteCollection];
        spyOn(fabricanteService, 'addFabricanteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        expect(fabricanteService.query).toHaveBeenCalled();
        expect(fabricanteService.addFabricanteToCollectionIfMissing).toHaveBeenCalledWith(fabricanteCollection, ...additionalFabricantes);
        expect(comp.fabricantesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CategoriaVeiculo query and add missing value', () => {
        const veiculo: IVeiculo = { id: 456 };
        const categoria: ICategoriaVeiculo = { id: 78800 };
        veiculo.categoria = categoria;

        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [{ id: 23717 }];
        spyOn(categoriaVeiculoService, 'query').and.returnValue(of(new HttpResponse({ body: categoriaVeiculoCollection })));
        const additionalCategoriaVeiculos = [categoria];
        const expectedCollection: ICategoriaVeiculo[] = [...additionalCategoriaVeiculos, ...categoriaVeiculoCollection];
        spyOn(categoriaVeiculoService, 'addCategoriaVeiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        expect(categoriaVeiculoService.query).toHaveBeenCalled();
        expect(categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
          categoriaVeiculoCollection,
          ...additionalCategoriaVeiculos
        );
        expect(comp.categoriaVeiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Usuario query and add missing value', () => {
        const veiculo: IVeiculo = { id: 456 };
        const motoristasHabilitados: IUsuario[] = [{ id: 18239 }];
        veiculo.motoristasHabilitados = motoristasHabilitados;

        const usuarioCollection: IUsuario[] = [{ id: 77200 }];
        spyOn(usuarioService, 'query').and.returnValue(of(new HttpResponse({ body: usuarioCollection })));
        const additionalUsuarios = [...motoristasHabilitados];
        const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
        spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        expect(usuarioService.query).toHaveBeenCalled();
        expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
        expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const veiculo: IVeiculo = { id: 456 };
        const fabricante: IFabricante = { id: 52532 };
        veiculo.fabricante = fabricante;
        const categoria: ICategoriaVeiculo = { id: 42025 };
        veiculo.categoria = categoria;
        const motoristasHabilitados: IUsuario = { id: 69846 };
        veiculo.motoristasHabilitados = [motoristasHabilitados];

        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(veiculo));
        expect(comp.fabricantesSharedCollection).toContain(fabricante);
        expect(comp.categoriaVeiculosSharedCollection).toContain(categoria);
        expect(comp.usuariosSharedCollection).toContain(motoristasHabilitados);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const veiculo = { id: 123 };
        spyOn(veiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: veiculo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(veiculoService.update).toHaveBeenCalledWith(veiculo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const veiculo = new Veiculo();
        spyOn(veiculoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: veiculo }));
        saveSubject.complete();

        // THEN
        expect(veiculoService.create).toHaveBeenCalledWith(veiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const veiculo = { id: 123 };
        spyOn(veiculoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ veiculo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(veiculoService.update).toHaveBeenCalledWith(veiculo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFabricanteById', () => {
        it('Should return tracked Fabricante primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFabricanteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCategoriaVeiculoById', () => {
        it('Should return tracked CategoriaVeiculo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoriaVeiculoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUsuarioById', () => {
        it('Should return tracked Usuario primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUsuarioById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUsuario', () => {
        it('Should return option if no Usuario is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUsuario(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Usuario for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUsuario(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Usuario is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUsuario(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
