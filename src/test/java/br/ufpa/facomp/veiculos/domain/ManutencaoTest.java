package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManutencaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manutencao.class);
        Manutencao manutencao1 = new Manutencao();
        manutencao1.setId(1L);
        Manutencao manutencao2 = new Manutencao();
        manutencao2.setId(manutencao1.getId());
        assertThat(manutencao1).isEqualTo(manutencao2);
        manutencao2.setId(2L);
        assertThat(manutencao1).isNotEqualTo(manutencao2);
        manutencao1.setId(null);
        assertThat(manutencao1).isNotEqualTo(manutencao2);
    }
}
