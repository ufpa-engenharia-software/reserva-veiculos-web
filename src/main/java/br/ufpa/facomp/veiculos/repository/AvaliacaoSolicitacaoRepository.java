package br.ufpa.facomp.veiculos.repository;

import br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AvaliacaoSolicitacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvaliacaoSolicitacaoRepository
    extends JpaRepository<AvaliacaoSolicitacao, Long>, JpaSpecificationExecutor<AvaliacaoSolicitacao> {}
