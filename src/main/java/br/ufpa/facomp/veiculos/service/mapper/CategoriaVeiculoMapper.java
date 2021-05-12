package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaVeiculo} and its DTO {@link CategoriaVeiculoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoriaVeiculoMapper extends EntityMapper<CategoriaVeiculoDTO, CategoriaVeiculo> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CategoriaVeiculoDTO toDtoNome(CategoriaVeiculo categoriaVeiculo);
}
