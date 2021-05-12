jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SolicitacaoService } from '../service/solicitacao.service';
import { ISolicitacao, Solicitacao } from '../solicitacao.model';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { SolicitacaoUpdateComponent } from './solicitacao-update.component';

describe('Component Tests', () => {
  describe('Solicitacao Management Update Component', () => {
    let comp: SolicitacaoUpdateComponent;
    let fixture: ComponentFixture<SolicitacaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let solicitacaoService: SolicitacaoService;
    let categoriaVeiculoService: CategoriaVeiculoService;
    let veiculoService: VeiculoService;
    let usuarioService: UsuarioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SolicitacaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SolicitacaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SolicitacaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      solicitacaoService = TestBed.inject(SolicitacaoService);
      categoriaVeiculoService = TestBed.inject(CategoriaVeiculoService);
      veiculoService = TestBed.inject(VeiculoService);
      usuarioService = TestBed.inject(UsuarioService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CategoriaVeiculo query and add missing value', () => {
        const solicitacao: ISolicitacao = { id: 456 };
        const categoria: ICategoriaVeiculo = { id: 81825 };
        solicitacao.categoria = categoria;

        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [{ id: 41340 }];
        spyOn(categoriaVeiculoService, 'query').and.returnValue(of(new HttpResponse({ body: categoriaVeiculoCollection })));
        const additionalCategoriaVeiculos = [categoria];
        const expectedCollection: ICategoriaVeiculo[] = [...additionalCategoriaVeiculos, ...categoriaVeiculoCollection];
        spyOn(categoriaVeiculoService, 'addCategoriaVeiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        expect(categoriaVeiculoService.query).toHaveBeenCalled();
        expect(categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
          categoriaVeiculoCollection,
          ...additionalCategoriaVeiculos
        );
        expect(comp.categoriaVeiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Veiculo query and add missing value', () => {
        const solicitacao: ISolicitacao = { id: 456 };
        const veiculoAlocado: IVeiculo = { id: 91966 };
        solicitacao.veiculoAlocado = veiculoAlocado;

        const veiculoCollection: IVeiculo[] = [{ id: 31607 }];
        spyOn(veiculoService, 'query').and.returnValue(of(new HttpResponse({ body: veiculoCollection })));
        const additionalVeiculos = [veiculoAlocado];
        const expectedCollection: IVeiculo[] = [...additionalVeiculos, ...veiculoCollection];
        spyOn(veiculoService, 'addVeiculoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        expect(veiculoService.query).toHaveBeenCalled();
        expect(veiculoService.addVeiculoToCollectionIfMissing).toHaveBeenCalledWith(veiculoCollection, ...additionalVeiculos);
        expect(comp.veiculosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Usuario query and add missing value', () => {
        const solicitacao: ISolicitacao = { id: 456 };
        const solicitante: IUsuario = { id: 30973 };
        solicitacao.solicitante = solicitante;
        const autorizador: IUsuario = { id: 12988 };
        solicitacao.autorizador = autorizador;
        const motorista: IUsuario = { id: 361 };
        solicitacao.motorista = motorista;

        const usuarioCollection: IUsuario[] = [{ id: 59669 }];
        spyOn(usuarioService, 'query').and.returnValue(of(new HttpResponse({ body: usuarioCollection })));
        const additionalUsuarios = [solicitante, autorizador, motorista];
        const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
        spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        expect(usuarioService.query).toHaveBeenCalled();
        expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
        expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const solicitacao: ISolicitacao = { id: 456 };
        const categoria: ICategoriaVeiculo = { id: 16114 };
        solicitacao.categoria = categoria;
        const veiculoAlocado: IVeiculo = { id: 31146 };
        solicitacao.veiculoAlocado = veiculoAlocado;
        const solicitante: IUsuario = { id: 62963 };
        solicitacao.solicitante = solicitante;
        const autorizador: IUsuario = { id: 94085 };
        solicitacao.autorizador = autorizador;
        const motorista: IUsuario = { id: 80795 };
        solicitacao.motorista = motorista;

        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(solicitacao));
        expect(comp.categoriaVeiculosSharedCollection).toContain(categoria);
        expect(comp.veiculosSharedCollection).toContain(veiculoAlocado);
        expect(comp.usuariosSharedCollection).toContain(solicitante);
        expect(comp.usuariosSharedCollection).toContain(autorizador);
        expect(comp.usuariosSharedCollection).toContain(motorista);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const solicitacao = { id: 123 };
        spyOn(solicitacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: solicitacao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(solicitacaoService.update).toHaveBeenCalledWith(solicitacao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const solicitacao = new Solicitacao();
        spyOn(solicitacaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: solicitacao }));
        saveSubject.complete();

        // THEN
        expect(solicitacaoService.create).toHaveBeenCalledWith(solicitacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const solicitacao = { id: 123 };
        spyOn(solicitacaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ solicitacao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(solicitacaoService.update).toHaveBeenCalledWith(solicitacao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategoriaVeiculoById', () => {
        it('Should return tracked CategoriaVeiculo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoriaVeiculoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackVeiculoById', () => {
        it('Should return tracked Veiculo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVeiculoById(0, entity);
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
  });
});
