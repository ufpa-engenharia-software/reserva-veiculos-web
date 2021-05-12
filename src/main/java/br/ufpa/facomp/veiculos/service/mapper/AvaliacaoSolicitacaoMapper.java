package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AvaliacaoSolicitacao} and its DTO {@link AvaliacaoSolicitacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { SolicitacaoMapper.class })
public interface AvaliacaoSolicitacaoMapper extends EntityMapper<AvaliacaoSolicitacaoDTO, AvaliacaoSolicitacao> {
    @Mapping(target = "solicitacao", source = "solicitacao", qualifiedByName = "id")
    AvaliacaoSolicitacaoDTO toDto(AvaliacaoSolicitacao s);
}
