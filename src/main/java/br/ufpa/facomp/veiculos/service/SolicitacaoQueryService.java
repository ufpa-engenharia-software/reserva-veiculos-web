package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.Solicitacao;
import br.ufpa.facomp.veiculos.repository.SolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.SolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.SolicitacaoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Solicitacao} entities in the database.
 * The main input is a {@link SolicitacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SolicitacaoDTO} or a {@link Page} of {@link SolicitacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SolicitacaoQueryService extends QueryService<Solicitacao> {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoQueryService.class);

    private final SolicitacaoRepository solicitacaoRepository;

    private final SolicitacaoMapper solicitacaoMapper;

    public SolicitacaoQueryService(SolicitacaoRepository solicitacaoRepository, SolicitacaoMapper solicitacaoMapper) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    /**
     * Return a {@link List} of {@link SolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoDTO> findByCriteria(SolicitacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoMapper.toDto(solicitacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SolicitacaoDTO> findByCriteria(SolicitacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoRepository.findAll(specification, page).map(solicitacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SolicitacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link SolicitacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Solicitacao> createSpecification(SolicitacaoCriteria criteria) {
        Specification<Solicitacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Solicitacao_.id));
            }
            if (criteria.getOrigem() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrigem(), Solicitacao_.origem));
            }
            if (criteria.getDestino() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestino(), Solicitacao_.destino));
            }
            if (criteria.getDataSolicitacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataSolicitacao(), Solicitacao_.dataSolicitacao));
            }
            if (criteria.getHorarioSaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHorarioSaida(), Solicitacao_.horarioSaida));
            }
            if (criteria.getHorarioRetorno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHorarioRetorno(), Solicitacao_.horarioRetorno));
            }
            if (criteria.getDistanciaEstimadaKm() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDistanciaEstimadaKm(), Solicitacao_.distanciaEstimadaKm));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Solicitacao_.status));
            }
            if (criteria.getnPessoas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getnPessoas(), Solicitacao_.nPessoas));
            }
            if (criteria.getPeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeso(), Solicitacao_.peso));
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Solicitacao_.categoria, JoinType.LEFT).get(CategoriaVeiculo_.id)
                        )
                    );
            }
            if (criteria.getVeiculoAlocadoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVeiculoAlocadoId(),
                            root -> root.join(Solicitacao_.veiculoAlocado, JoinType.LEFT).get(Veiculo_.id)
                        )
                    );
            }
            if (criteria.getAvaliacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAvaliacaoId(),
                            root -> root.join(Solicitacao_.avaliacao, JoinType.LEFT).get(AvaliacaoSolicitacao_.id)
                        )
                    );
            }
            if (criteria.getSolicitanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitanteId(),
                            root -> root.join(Solicitacao_.solicitante, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
            if (criteria.getAutorizadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAutorizadorId(),
                            root -> root.join(Solicitacao_.autorizador, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
            if (criteria.getMotoristaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotoristaId(),
                            root -> root.join(Solicitacao_.motorista, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
