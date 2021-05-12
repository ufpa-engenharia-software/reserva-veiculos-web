package br.ufpa.facomp.veiculos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaVeiculoMapperTest {

    private CategoriaVeiculoMapper categoriaVeiculoMapper;

    @BeforeEach
    public void setUp() {
        categoriaVeiculoMapper = new CategoriaVeiculoMapperImpl();
    }
}
