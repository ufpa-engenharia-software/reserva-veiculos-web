package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.ufpa.facomp.veiculos.domain.Manutencao}.
 */
public interface ManutencaoService {
    /**
     * Save a manutencao.
     *
     * @param manutencaoDTO the entity to save.
     * @return the persisted entity.
     */
    ManutencaoDTO save(ManutencaoDTO manutencaoDTO);

    /**
     * Partially updates a manutencao.
     *
     * @param manutencaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManutencaoDTO> partialUpdate(ManutencaoDTO manutencaoDTO);

    /**
     * Get all the manutencaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManutencaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" manutencao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManutencaoDTO> findOne(Long id);

    /**
     * Delete the "id" manutencao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
