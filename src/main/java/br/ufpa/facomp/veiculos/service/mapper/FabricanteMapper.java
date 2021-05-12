package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.FabricanteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fabricante} and its DTO {@link FabricanteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FabricanteMapper extends EntityMapper<FabricanteDTO, Fabricante> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    FabricanteDTO toDtoNome(Fabricante fabricante);
}
