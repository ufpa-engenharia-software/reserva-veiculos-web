jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UsuarioService } from '../service/usuario.service';
import { IUsuario, Usuario } from '../usuario.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UsuarioUpdateComponent } from './usuario-update.component';

describe('Component Tests', () => {
  describe('Usuario Management Update Component', () => {
    let comp: UsuarioUpdateComponent;
    let fixture: ComponentFixture<UsuarioUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let usuarioService: UsuarioService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UsuarioUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UsuarioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsuarioUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      usuarioService = TestBed.inject(UsuarioService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const usuario: IUsuario = { id: 456 };
        const user: IUser = { id: 27699 };
        usuario.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ usuario });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const usuario: IUsuario = { id: 456 };
        const user: IUser = { id: 47918 };
        usuario.user = user;

        activatedRoute.data = of({ usuario });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(usuario));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const usuario = { id: 123 };
        spyOn(usuarioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuario }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(usuarioService.update).toHaveBeenCalledWith(usuario);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const usuario = new Usuario();
        spyOn(usuarioService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuario }));
        saveSubject.complete();

        // THEN
        expect(usuarioService.create).toHaveBeenCalledWith(usuario);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const usuario = { id: 123 };
        spyOn(usuarioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuario });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(usuarioService.update).toHaveBeenCalledWith(usuario);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
