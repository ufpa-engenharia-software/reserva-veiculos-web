package br.ufpa.facomp.veiculos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolicitacaoMapperTest {

    private SolicitacaoMapper solicitacaoMapper;

    @BeforeEach
    public void setUp() {
        solicitacaoMapper = new SolicitacaoMapperImpl();
    }
}
