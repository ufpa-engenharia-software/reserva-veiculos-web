import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VeiculoDetailComponent } from './veiculo-detail.component';

describe('Component Tests', () => {
  describe('Veiculo Management Detail Component', () => {
    let comp: VeiculoDetailComponent;
    let fixture: ComponentFixture<VeiculoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VeiculoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ veiculo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VeiculoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VeiculoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load veiculo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.veiculo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
