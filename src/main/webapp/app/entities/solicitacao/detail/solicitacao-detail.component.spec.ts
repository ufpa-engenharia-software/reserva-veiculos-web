import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { SolicitacaoDetailComponent } from './solicitacao-detail.component';

describe('Component Tests', () => {
  describe('Solicitacao Management Detail Component', () => {
    let comp: SolicitacaoDetailComponent;
    let fixture: ComponentFixture<SolicitacaoDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SolicitacaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ solicitacao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SolicitacaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolicitacaoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load solicitacao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.solicitacao).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
