package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.Usuario;
import br.ufpa.facomp.veiculos.repository.UsuarioRepository;
import br.ufpa.facomp.veiculos.service.UsuarioService;
import br.ufpa.facomp.veiculos.service.dto.UsuarioDTO;
import br.ufpa.facomp.veiculos.service.mapper.UsuarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Usuario}.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        log.debug("Request to save Usuario : {}", usuarioDTO);
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public Optional<UsuarioDTO> partialUpdate(UsuarioDTO usuarioDTO) {
        log.debug("Request to partially update Usuario : {}", usuarioDTO);

        return usuarioRepository
            .findById(usuarioDTO.getId())
            .map(
                existingUsuario -> {
                    usuarioMapper.partialUpdate(existingUsuario, usuarioDTO);
                    return existingUsuario;
                }
            )
            .map(usuarioRepository::save)
            .map(usuarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Usuarios");
        return usuarioRepository.findAll(pageable).map(usuarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> findOne(Long id) {
        log.debug("Request to get Usuario : {}", id);
        return usuarioRepository.findById(id).map(usuarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
    }
}
