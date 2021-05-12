package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.ufpa.facomp.veiculos.domain.CategoriaVeiculo}.
 */
public interface CategoriaVeiculoService {
    /**
     * Save a categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaVeiculoDTO save(CategoriaVeiculoDTO categoriaVeiculoDTO);

    /**
     * Partially updates a categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaVeiculoDTO> partialUpdate(CategoriaVeiculoDTO categoriaVeiculoDTO);

    /**
     * Get all the categoriaVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoriaVeiculoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" categoriaVeiculo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaVeiculoDTO> findOne(Long id);

    /**
     * Delete the "id" categoriaVeiculo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
