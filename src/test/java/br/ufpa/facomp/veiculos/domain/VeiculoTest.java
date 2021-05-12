package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Veiculo.class);
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setId(1L);
        Veiculo veiculo2 = new Veiculo();
        veiculo2.setId(veiculo1.getId());
        assertThat(veiculo1).isEqualTo(veiculo2);
        veiculo2.setId(2L);
        assertThat(veiculo1).isNotEqualTo(veiculo2);
        veiculo1.setId(null);
        assertThat(veiculo1).isNotEqualTo(veiculo2);
    }
}
