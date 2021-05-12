package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManutencaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManutencaoDTO.class);
        ManutencaoDTO manutencaoDTO1 = new ManutencaoDTO();
        manutencaoDTO1.setId(1L);
        ManutencaoDTO manutencaoDTO2 = new ManutencaoDTO();
        assertThat(manutencaoDTO1).isNotEqualTo(manutencaoDTO2);
        manutencaoDTO2.setId(manutencaoDTO1.getId());
        assertThat(manutencaoDTO1).isEqualTo(manutencaoDTO2);
        manutencaoDTO2.setId(2L);
        assertThat(manutencaoDTO1).isNotEqualTo(manutencaoDTO2);
        manutencaoDTO1.setId(null);
        assertThat(manutencaoDTO1).isNotEqualTo(manutencaoDTO2);
    }
}
