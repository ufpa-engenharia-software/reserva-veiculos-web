package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FabricanteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FabricanteDTO.class);
        FabricanteDTO fabricanteDTO1 = new FabricanteDTO();
        fabricanteDTO1.setId(1L);
        FabricanteDTO fabricanteDTO2 = new FabricanteDTO();
        assertThat(fabricanteDTO1).isNotEqualTo(fabricanteDTO2);
        fabricanteDTO2.setId(fabricanteDTO1.getId());
        assertThat(fabricanteDTO1).isEqualTo(fabricanteDTO2);
        fabricanteDTO2.setId(2L);
        assertThat(fabricanteDTO1).isNotEqualTo(fabricanteDTO2);
        fabricanteDTO1.setId(null);
        assertThat(fabricanteDTO1).isNotEqualTo(fabricanteDTO2);
    }
}
