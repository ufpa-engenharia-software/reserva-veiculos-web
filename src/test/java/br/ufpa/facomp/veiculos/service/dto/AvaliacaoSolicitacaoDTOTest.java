package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvaliacaoSolicitacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvaliacaoSolicitacaoDTO.class);
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO1 = new AvaliacaoSolicitacaoDTO();
        avaliacaoSolicitacaoDTO1.setId(1L);
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO2 = new AvaliacaoSolicitacaoDTO();
        assertThat(avaliacaoSolicitacaoDTO1).isNotEqualTo(avaliacaoSolicitacaoDTO2);
        avaliacaoSolicitacaoDTO2.setId(avaliacaoSolicitacaoDTO1.getId());
        assertThat(avaliacaoSolicitacaoDTO1).isEqualTo(avaliacaoSolicitacaoDTO2);
        avaliacaoSolicitacaoDTO2.setId(2L);
        assertThat(avaliacaoSolicitacaoDTO1).isNotEqualTo(avaliacaoSolicitacaoDTO2);
        avaliacaoSolicitacaoDTO1.setId(null);
        assertThat(avaliacaoSolicitacaoDTO1).isNotEqualTo(avaliacaoSolicitacaoDTO2);
    }
}
