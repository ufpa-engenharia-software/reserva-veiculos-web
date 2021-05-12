package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvaliacaoSolicitacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvaliacaoSolicitacao.class);
        AvaliacaoSolicitacao avaliacaoSolicitacao1 = new AvaliacaoSolicitacao();
        avaliacaoSolicitacao1.setId(1L);
        AvaliacaoSolicitacao avaliacaoSolicitacao2 = new AvaliacaoSolicitacao();
        avaliacaoSolicitacao2.setId(avaliacaoSolicitacao1.getId());
        assertThat(avaliacaoSolicitacao1).isEqualTo(avaliacaoSolicitacao2);
        avaliacaoSolicitacao2.setId(2L);
        assertThat(avaliacaoSolicitacao1).isNotEqualTo(avaliacaoSolicitacao2);
        avaliacaoSolicitacao1.setId(null);
        assertThat(avaliacaoSolicitacao1).isNotEqualTo(avaliacaoSolicitacao2);
    }
}
