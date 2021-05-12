import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaVeiculoDetailComponent } from './categoria-veiculo-detail.component';

describe('Component Tests', () => {
  describe('CategoriaVeiculo Management Detail Component', () => {
    let comp: CategoriaVeiculoDetailComponent;
    let fixture: ComponentFixture<CategoriaVeiculoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CategoriaVeiculoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ categoriaVeiculo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CategoriaVeiculoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CategoriaVeiculoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load categoriaVeiculo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.categoriaVeiculo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
