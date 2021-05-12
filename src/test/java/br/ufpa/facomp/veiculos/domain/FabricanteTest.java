package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FabricanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fabricante.class);
        Fabricante fabricante1 = new Fabricante();
        fabricante1.setId(1L);
        Fabricante fabricante2 = new Fabricante();
        fabricante2.setId(fabricante1.getId());
        assertThat(fabricante1).isEqualTo(fabricante2);
        fabricante2.setId(2L);
        assertThat(fabricante1).isNotEqualTo(fabricante2);
        fabricante1.setId(null);
        assertThat(fabricante1).isNotEqualTo(fabricante2);
    }
}
