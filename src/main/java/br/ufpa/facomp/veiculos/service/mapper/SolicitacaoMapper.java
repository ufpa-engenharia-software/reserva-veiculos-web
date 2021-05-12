package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Solicitacao} and its DTO {@link SolicitacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoriaVeiculoMapper.class, VeiculoMapper.class, UsuarioMapper.class })
public interface SolicitacaoMapper extends EntityMapper<SolicitacaoDTO, Solicitacao> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "nome")
    @Mapping(target = "veiculoAlocado", source = "veiculoAlocado", qualifiedByName = "placa")
    @Mapping(target = "solicitante", source = "solicitante", qualifiedByName = "nome")
    @Mapping(target = "autorizador", source = "autorizador", qualifiedByName = "nome")
    @Mapping(target = "motorista", source = "motorista", qualifiedByName = "nome")
    SolicitacaoDTO toDto(Solicitacao s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SolicitacaoDTO toDtoId(Solicitacao solicitacao);
}
