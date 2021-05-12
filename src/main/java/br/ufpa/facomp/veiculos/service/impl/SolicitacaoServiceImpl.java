package br.ufpa.facomp.veiculos.service.impl;

import br.ufpa.facomp.veiculos.domain.Solicitacao;
import br.ufpa.facomp.veiculos.repository.SolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.SolicitacaoService;
import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.SolicitacaoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Solicitacao}.
 */
@Service
@Transactional
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoServiceImpl.class);

    private final SolicitacaoRepository solicitacaoRepository;

    private final SolicitacaoMapper solicitacaoMapper;

    public SolicitacaoServiceImpl(SolicitacaoRepository solicitacaoRepository, SolicitacaoMapper solicitacaoMapper) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    @Override
    public SolicitacaoDTO save(SolicitacaoDTO solicitacaoDTO) {
        log.debug("Request to save Solicitacao : {}", solicitacaoDTO);
        Solicitacao solicitacao = solicitacaoMapper.toEntity(solicitacaoDTO);
        solicitacao = solicitacaoRepository.save(solicitacao);
        return solicitacaoMapper.toDto(solicitacao);
    }

    @Override
    public Optional<SolicitacaoDTO> partialUpdate(SolicitacaoDTO solicitacaoDTO) {
        log.debug("Request to partially update Solicitacao : {}", solicitacaoDTO);

        return solicitacaoRepository
            .findById(solicitacaoDTO.getId())
            .map(
                existingSolicitacao -> {
                    solicitacaoMapper.partialUpdate(existingSolicitacao, solicitacaoDTO);
                    return existingSolicitacao;
                }
            )
            .map(solicitacaoRepository::save)
            .map(solicitacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Solicitacaos");
        return solicitacaoRepository.findAll(pageable).map(solicitacaoMapper::toDto);
    }

    /**
     *  Get all the solicitacaos where Avaliacao is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoDTO> findAllWhereAvaliacaoIsNull() {
        log.debug("Request to get all solicitacaos where Avaliacao is null");
        return StreamSupport
            .stream(solicitacaoRepository.findAll().spliterator(), false)
            .filter(solicitacao -> solicitacao.getAvaliacao() == null)
            .map(solicitacaoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitacaoDTO> findOne(Long id) {
        log.debug("Request to get Solicitacao : {}", id);
        return solicitacaoRepository.findById(id).map(solicitacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Solicitacao : {}", id);
        solicitacaoRepository.deleteById(id);
    }
}
