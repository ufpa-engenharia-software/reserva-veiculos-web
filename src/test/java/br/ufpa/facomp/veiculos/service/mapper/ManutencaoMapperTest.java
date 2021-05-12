package br.ufpa.facomp.veiculos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManutencaoMapperTest {

    private ManutencaoMapper manutencaoMapper;

    @BeforeEach
    public void setUp() {
        manutencaoMapper = new ManutencaoMapperImpl();
    }
}
