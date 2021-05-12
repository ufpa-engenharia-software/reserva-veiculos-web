package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VeiculoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VeiculoDTO.class);
        VeiculoDTO veiculoDTO1 = new VeiculoDTO();
        veiculoDTO1.setId(1L);
        VeiculoDTO veiculoDTO2 = new VeiculoDTO();
        assertThat(veiculoDTO1).isNotEqualTo(veiculoDTO2);
        veiculoDTO2.setId(veiculoDTO1.getId());
        assertThat(veiculoDTO1).isEqualTo(veiculoDTO2);
        veiculoDTO2.setId(2L);
        assertThat(veiculoDTO1).isNotEqualTo(veiculoDTO2);
        veiculoDTO1.setId(null);
        assertThat(veiculoDTO1).isNotEqualTo(veiculoDTO2);
    }
}
