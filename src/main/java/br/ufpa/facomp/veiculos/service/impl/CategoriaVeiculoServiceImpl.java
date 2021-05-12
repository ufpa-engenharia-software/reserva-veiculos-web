package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import br.ufpa.facomp.veiculos.repository.CategoriaVeiculoRepository;
import br.ufpa.facomp.veiculos.service.CategoriaVeiculoService;
import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.CategoriaVeiculoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaVeiculo}.
 */
@Service
@Transactional
public class CategoriaVeiculoServiceImpl implements CategoriaVeiculoService {

    private final Logger log = LoggerFactory.getLogger(CategoriaVeiculoServiceImpl.class);

    private final CategoriaVeiculoRepository categoriaVeiculoRepository;

    private final CategoriaVeiculoMapper categoriaVeiculoMapper;

    public CategoriaVeiculoServiceImpl(
        CategoriaVeiculoRepository categoriaVeiculoRepository,
        CategoriaVeiculoMapper categoriaVeiculoMapper
    ) {
        this.categoriaVeiculoRepository = categoriaVeiculoRepository;
        this.categoriaVeiculoMapper = categoriaVeiculoMapper;
    }

    @Override
    public CategoriaVeiculoDTO save(CategoriaVeiculoDTO categoriaVeiculoDTO) {
        log.debug("Request to save CategoriaVeiculo : {}", categoriaVeiculoDTO);
        CategoriaVeiculo categoriaVeiculo = categoriaVeiculoMapper.toEntity(categoriaVeiculoDTO);
        categoriaVeiculo = categoriaVeiculoRepository.save(categoriaVeiculo);
        return categoriaVeiculoMapper.toDto(categoriaVeiculo);
    }

    @Override
    public Optional<CategoriaVeiculoDTO> partialUpdate(CategoriaVeiculoDTO categoriaVeiculoDTO) {
        log.debug("Request to partially update CategoriaVeiculo : {}", categoriaVeiculoDTO);

        return categoriaVeiculoRepository
            .findById(categoriaVeiculoDTO.getId())
            .map(
                existingCategoriaVeiculo -> {
                    categoriaVeiculoMapper.partialUpdate(existingCategoriaVeiculo, categoriaVeiculoDTO);
                    return existingCategoriaVeiculo;
                }
            )
            .map(categoriaVeiculoRepository::save)
            .map(categoriaVeiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaVeiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoriaVeiculos");
        return categoriaVeiculoRepository.findAll(pageable).map(categoriaVeiculoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaVeiculoDTO> findOne(Long id) {
        log.debug("Request to get CategoriaVeiculo : {}", id);
        return categoriaVeiculoRepository.findById(id).map(categoriaVeiculoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoriaVeiculo : {}", id);
        categoriaVeiculoRepository.deleteById(id);
    }
}
