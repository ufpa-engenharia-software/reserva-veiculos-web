package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.Manutencao;
import br.ufpa.facomp.veiculos.repository.ManutencaoRepository;
import br.ufpa.facomp.veiculos.service.ManutencaoService;
import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.ManutencaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Manutencao}.
 */
@Service
@Transactional
public class ManutencaoServiceImpl implements ManutencaoService {

    private final Logger log = LoggerFactory.getLogger(ManutencaoServiceImpl.class);

    private final ManutencaoRepository manutencaoRepository;

    private final ManutencaoMapper manutencaoMapper;

    public ManutencaoServiceImpl(ManutencaoRepository manutencaoRepository, ManutencaoMapper manutencaoMapper) {
        this.manutencaoRepository = manutencaoRepository;
        this.manutencaoMapper = manutencaoMapper;
    }

    @Override
    public ManutencaoDTO save(ManutencaoDTO manutencaoDTO) {
        log.debug("Request to save Manutencao : {}", manutencaoDTO);
        Manutencao manutencao = manutencaoMapper.toEntity(manutencaoDTO);
        manutencao = manutencaoRepository.save(manutencao);
        return manutencaoMapper.toDto(manutencao);
    }

    @Override
    public Optional<ManutencaoDTO> partialUpdate(ManutencaoDTO manutencaoDTO) {
        log.debug("Request to partially update Manutencao : {}", manutencaoDTO);

        return manutencaoRepository
            .findById(manutencaoDTO.getId())
            .map(
                existingManutencao -> {
                    manutencaoMapper.partialUpdate(existingManutencao, manutencaoDTO);
                    return existingManutencao;
                }
            )
            .map(manutencaoRepository::save)
            .map(manutencaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManutencaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Manutencaos");
        return manutencaoRepository.findAll(pageable).map(manutencaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManutencaoDTO> findOne(Long id) {
        log.debug("Request to get Manutencao : {}", id);
        return manutencaoRepository.findById(id).map(manutencaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Manutencao : {}", id);
        manutencaoRepository.deleteById(id);
    }
}
