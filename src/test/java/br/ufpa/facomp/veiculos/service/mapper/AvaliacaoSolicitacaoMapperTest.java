package br.ufpa.facomp.veiculos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AvaliacaoSolicitacaoMapperTest {

    private AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper;

    @BeforeEach
    public void setUp() {
        avaliacaoSolicitacaoMapper = new AvaliacaoSolicitacaoMapperImpl();
    }
}
