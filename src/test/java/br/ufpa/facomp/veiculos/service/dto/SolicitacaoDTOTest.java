package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolicitacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoDTO.class);
        SolicitacaoDTO solicitacaoDTO1 = new SolicitacaoDTO();
        solicitacaoDTO1.setId(1L);
        SolicitacaoDTO solicitacaoDTO2 = new SolicitacaoDTO();
        assertThat(solicitacaoDTO1).isNotEqualTo(solicitacaoDTO2);
        solicitacaoDTO2.setId(solicitacaoDTO1.getId());
        assertThat(solicitacaoDTO1).isEqualTo(solicitacaoDTO2);
        solicitacaoDTO2.setId(2L);
        assertThat(solicitacaoDTO1).isNotEqualTo(solicitacaoDTO2);
        solicitacaoDTO1.setId(null);
        assertThat(solicitacaoDTO1).isNotEqualTo(solicitacaoDTO2);
    }
}
