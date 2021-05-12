package br.ufpa.facomp.veiculos.repository;

import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CategoriaVeiculo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaVeiculoRepository extends JpaRepository<CategoriaVeiculo, Long>, JpaSpecificationExecutor<CategoriaVeiculo> {}
