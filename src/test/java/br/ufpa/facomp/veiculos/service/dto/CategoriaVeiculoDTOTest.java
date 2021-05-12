package br.ufpa.facomp.veiculos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaVeiculoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaVeiculoDTO.class);
        CategoriaVeiculoDTO categoriaVeiculoDTO1 = new CategoriaVeiculoDTO();
        categoriaVeiculoDTO1.setId(1L);
        CategoriaVeiculoDTO categoriaVeiculoDTO2 = new CategoriaVeiculoDTO();
        assertThat(categoriaVeiculoDTO1).isNotEqualTo(categoriaVeiculoDTO2);
        categoriaVeiculoDTO2.setId(categoriaVeiculoDTO1.getId());
        assertThat(categoriaVeiculoDTO1).isEqualTo(categoriaVeiculoDTO2);
        categoriaVeiculoDTO2.setId(2L);
        assertThat(categoriaVeiculoDTO1).isNotEqualTo(categoriaVeiculoDTO2);
        categoriaVeiculoDTO1.setId(null);
        assertThat(categoriaVeiculoDTO1).isNotEqualTo(categoriaVeiculoDTO2);
    }
}
