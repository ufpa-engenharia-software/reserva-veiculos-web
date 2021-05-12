package br.ufpa.facomp.veiculos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VeiculoMapperTest {

    private VeiculoMapper veiculoMapper;

    @BeforeEach
    public void setUp() {
        veiculoMapper = new VeiculoMapperImpl();
    }
}
