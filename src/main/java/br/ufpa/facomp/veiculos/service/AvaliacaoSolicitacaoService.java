package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao}.
 */
public interface AvaliacaoSolicitacaoService {
    /**
     * Save a avaliacaoSolicitacao.
     *
     * @param avaliacaoSolicitacaoDTO the entity to save.
     * @return the persisted entity.
     */
    AvaliacaoSolicitacaoDTO save(AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO);

    /**
     * Partially updates a avaliacaoSolicitacao.
     *
     * @param avaliacaoSolicitacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AvaliacaoSolicitacaoDTO> partialUpdate(AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO);

    /**
     * Get all the avaliacaoSolicitacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AvaliacaoSolicitacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" avaliacaoSolicitacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AvaliacaoSolicitacaoDTO> findOne(Long id);

    /**
     * Delete the "id" avaliacaoSolicitacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
