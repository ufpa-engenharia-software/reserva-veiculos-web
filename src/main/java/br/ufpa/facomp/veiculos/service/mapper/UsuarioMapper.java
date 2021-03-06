package br.ufpa.facomp.veiculos.service.mapper;

import br.ufpa.facomp.veiculos.domain.*;
import br.ufpa.facomp.veiculos.service.dto.UsuarioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UsuarioDTO toDto(Usuario s);

    @Named("nomeSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    Set<UsuarioDTO> toDtoNomeSet(Set<Usuario> usuario);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    UsuarioDTO toDtoNome(Usuario usuario);
}
