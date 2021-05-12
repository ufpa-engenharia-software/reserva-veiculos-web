package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.ufpa.facomp.veiculos.domain.Solicitacao}.
 */
public interface SolicitacaoService {
    /**
     * Save a solicitacao.
     *
     * @param solicitacaoDTO the entity to save.
     * @return the persisted entity.
     */
    SolicitacaoDTO save(SolicitacaoDTO solicitacaoDTO);

    /**
     * Partially updates a solicitacao.
     *
     * @param solicitacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SolicitacaoDTO> partialUpdate(SolicitacaoDTO solicitacaoDTO);

    /**
     * Get all the solicitacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolicitacaoDTO> findAll(Pageable pageable);
    /**
     * Get all the SolicitacaoDTO where Avaliacao is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<SolicitacaoDTO> findAllWhereAvaliacaoIsNull();

    /**
     * Get the "id" solicitacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolicitacaoDTO> findOne(Long id);

    /**
     * Delete the "id" solicitacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
