package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.ufpa.facomp.veiculos.domain.Veiculo}.
 */
public interface VeiculoService {
    /**
     * Save a veiculo.
     *
     * @param veiculoDTO the entity to save.
     * @return the persisted entity.
     */
    VeiculoDTO save(VeiculoDTO veiculoDTO);

    /**
     * Partially updates a veiculo.
     *
     * @param veiculoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VeiculoDTO> partialUpdate(VeiculoDTO veiculoDTO);

    /**
     * Get all the veiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VeiculoDTO> findAll(Pageable pageable);

    /**
     * Get all the veiculos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VeiculoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" veiculo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VeiculoDTO> findOne(Long id);

    /**
     * Delete the "id" veiculo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
