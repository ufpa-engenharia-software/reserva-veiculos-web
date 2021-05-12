package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manutencao} and its DTO {@link ManutencaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { VeiculoMapper.class })
public interface ManutencaoMapper extends EntityMapper<ManutencaoDTO, Manutencao> {
    @Mapping(target = "veiculo", source = "veiculo", qualifiedByName = "placa")
    ManutencaoDTO toDto(Manutencao s);
}
