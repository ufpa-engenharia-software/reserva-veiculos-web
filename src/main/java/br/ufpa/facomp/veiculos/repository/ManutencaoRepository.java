package br.ufpa.facomp.veiculos.repository;

import br.ufpa.facomp.veiculos.domain.Manutencao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Manutencao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long>, JpaSpecificationExecutor<Manutencao> {}
