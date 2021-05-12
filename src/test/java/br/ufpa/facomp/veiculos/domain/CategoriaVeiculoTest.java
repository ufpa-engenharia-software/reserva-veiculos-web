package br.ufpa.facomp.veiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.ufpa.facomp.veiculos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaVeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaVeiculo.class);
        CategoriaVeiculo categoriaVeiculo1 = new CategoriaVeiculo();
        categoriaVeiculo1.setId(1L);
        CategoriaVeiculo categoriaVeiculo2 = new CategoriaVeiculo();
        categoriaVeiculo2.setId(categoriaVeiculo1.getId());
        assertThat(categoriaVeiculo1).isEqualTo(categoriaVeiculo2);
        categoriaVeiculo2.setId(2L);
        assertThat(categoriaVeiculo1).isNotEqualTo(categoriaVeiculo2);
        categoriaVeiculo1.setId(null);
        assertThat(categoriaVeiculo1).isNotEqualTo(categoriaVeiculo2);
    }
}
