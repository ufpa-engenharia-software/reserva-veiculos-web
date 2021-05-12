import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FabricanteDetailComponent } from './fabricante-detail.component';

describe('Component Tests', () => {
  describe('Fabricante Management Detail Component', () => {
    let comp: FabricanteDetailComponent;
    let fixture: ComponentFixture<FabricanteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FabricanteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fabricante: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FabricanteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FabricanteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fabricante on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fabricante).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
