package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Veiculo} and its DTO {@link VeiculoDTO}.
 */
@Mapper(componentModel = "spring", uses = { FabricanteMapper.class, CategoriaVeiculoMapper.class, UsuarioMapper.class })
public interface VeiculoMapper extends EntityMapper<VeiculoDTO, Veiculo> {
    @Mapping(target = "fabricante", source = "fabricante", qualifiedByName = "nome")
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "nome")
    @Mapping(target = "motoristasHabilitados", source = "motoristasHabilitados", qualifiedByName = "nomeSet")
    VeiculoDTO toDto(Veiculo s);

    @Mapping(target = "removeMotoristasHabilitados", ignore = true)
    Veiculo toEntity(VeiculoDTO veiculoDTO);

    @Named("placa")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "placa", source = "placa")
    VeiculoDTO toDtoPlaca(Veiculo veiculo);
}
