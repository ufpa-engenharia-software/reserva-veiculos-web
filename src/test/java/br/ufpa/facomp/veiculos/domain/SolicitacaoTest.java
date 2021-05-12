package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolicitacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Solicitacao.class);
        Solicitacao solicitacao1 = new Solicitacao();
        solicitacao1.setId(1L);
        Solicitacao solicitacao2 = new Solicitacao();
        solicitacao2.setId(solicitacao1.getId());
        assertThat(solicitacao1).isEqualTo(solicitacao2);
        solicitacao2.setId(2L);
        assertThat(solicitacao1).isNotEqualTo(solicitacao2);
        solicitacao1.setId(null);
        assertThat(solicitacao1).isNotEqualTo(solicitacao2);
    }
}
