package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.repository.VeiculoRepository;
import br.ufpa.facomp.veiculos.service.VeiculoService;
import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.VeiculoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Veiculo}.
 */
@Service
@Transactional
public class VeiculoServiceImpl implements VeiculoService {

    private final Logger log = LoggerFactory.getLogger(VeiculoServiceImpl.class);

    private final VeiculoRepository veiculoRepository;

    private final VeiculoMapper veiculoMapper;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
    }

    @Override
    public VeiculoDTO save(VeiculoDTO veiculoDTO) {
        log.debug("Request to save Veiculo : {}", veiculoDTO);
        Veiculo veiculo = veiculoMapper.toEntity(veiculoDTO);
        veiculo = veiculoRepository.save(veiculo);
        return veiculoMapper.toDto(veiculo);
    }

    @Override
    public Optional<VeiculoDTO> partialUpdate(VeiculoDTO veiculoDTO) {
        log.debug("Request to partially update Veiculo : {}", veiculoDTO);

        return veiculoRepository
            .findById(veiculoDTO.getId())
            .map(
                existingVeiculo -> {
                    veiculoMapper.partialUpdate(existingVeiculo, veiculoDTO);
                    return existingVeiculo;
                }
            )
            .map(veiculoRepository::save)
            .map(veiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VeiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Veiculos");
        return veiculoRepository.findAll(pageable).map(veiculoMapper::toDto);
    }

    public Page<VeiculoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return veiculoRepository.findAllWithEagerRelationships(pageable).map(veiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VeiculoDTO> findOne(Long id) {
        log.debug("Request to get Veiculo : {}", id);
        return veiculoRepository.findOneWithEagerRelationships(id).map(veiculoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Veiculo : {}", id);
        veiculoRepository.deleteById(id);
    }
}
