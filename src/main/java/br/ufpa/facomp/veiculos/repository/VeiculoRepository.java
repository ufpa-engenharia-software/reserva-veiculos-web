package br.ufpa.facomp.veiculos.repository;

import br.ufpa.facomp.veiculos.domain.Veiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Veiculo entity.
 */
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {
    @Query(
        value = "select distinct veiculo from Veiculo veiculo left join fetch veiculo.motoristasHabilitados",
        countQuery = "select count(distinct veiculo) from Veiculo veiculo"
    )
    Page<Veiculo> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct veiculo from Veiculo veiculo left join fetch veiculo.motoristasHabilitados")
    List<Veiculo> findAllWithEagerRelationships();

    @Query("select veiculo from Veiculo veiculo left join fetch veiculo.motoristasHabilitados where veiculo.id =:id")
    Optional<Veiculo> findOneWithEagerRelationships(@Param("id") Long id);
}
