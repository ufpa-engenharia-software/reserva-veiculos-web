package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao;
import br.ufpa.facomp.veiculos.repository.AvaliacaoSolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.AvaliacaoSolicitacaoService;
import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.AvaliacaoSolicitacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AvaliacaoSolicitacao}.
 */
@Service
@Transactional
public class AvaliacaoSolicitacaoServiceImpl implements AvaliacaoSolicitacaoService {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoSolicitacaoServiceImpl.class);

    private final AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository;

    private final AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper;

    public AvaliacaoSolicitacaoServiceImpl(
        AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository,
        AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper
    ) {
        this.avaliacaoSolicitacaoRepository = avaliacaoSolicitacaoRepository;
        this.avaliacaoSolicitacaoMapper = avaliacaoSolicitacaoMapper;
    }

    @Override
    public AvaliacaoSolicitacaoDTO save(AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO) {
        log.debug("Request to save AvaliacaoSolicitacao : {}", avaliacaoSolicitacaoDTO);
        AvaliacaoSolicitacao avaliacaoSolicitacao = avaliacaoSolicitacaoMapper.toEntity(avaliacaoSolicitacaoDTO);
        avaliacaoSolicitacao = avaliacaoSolicitacaoRepository.save(avaliacaoSolicitacao);
        return avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);
    }

    @Override
    public Optional<AvaliacaoSolicitacaoDTO> partialUpdate(AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO) {
        log.debug("Request to partially update AvaliacaoSolicitacao : {}", avaliacaoSolicitacaoDTO);

        return avaliacaoSolicitacaoRepository
            .findById(avaliacaoSolicitacaoDTO.getId())
            .map(
                existingAvaliacaoSolicitacao -> {
                    avaliacaoSolicitacaoMapper.partialUpdate(existingAvaliacaoSolicitacao, avaliacaoSolicitacaoDTO);
                    return existingAvaliacaoSolicitacao;
                }
            )
            .map(avaliacaoSolicitacaoRepository::save)
            .map(avaliacaoSolicitacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AvaliacaoSolicitacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AvaliacaoSolicitacaos");
        return avaliacaoSolicitacaoRepository.findAll(pageable).map(avaliacaoSolicitacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AvaliacaoSolicitacaoDTO> findOne(Long id) {
        log.debug("Request to get AvaliacaoSolicitacao : {}", id);
        return avaliacaoSolicitacaoRepository.findById(id).map(avaliacaoSolicitacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AvaliacaoSolicitacao : {}", id);
        avaliacaoSolicitacaoRepository.deleteById(id);
    }
}
