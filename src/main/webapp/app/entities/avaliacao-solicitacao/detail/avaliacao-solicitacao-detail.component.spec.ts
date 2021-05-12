import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { AvaliacaoSolicitacaoDetailComponent } from './avaliacao-solicitacao-detail.component';

describe('Component Tests', () => {
  describe('AvaliacaoSolicitacao Management Detail Component', () => {
    let comp: AvaliacaoSolicitacaoDetailComponent;
    let fixture: ComponentFixture<AvaliacaoSolicitacaoDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AvaliacaoSolicitacaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ avaliacaoSolicitacao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AvaliacaoSolicitacaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AvaliacaoSolicitacaoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load avaliacaoSolicitacao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.avaliacaoSolicitacao).toEqual(jasmine.objectContaining({ id: 123 }));
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
