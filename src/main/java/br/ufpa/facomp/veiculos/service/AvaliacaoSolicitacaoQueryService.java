package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao;
import br.ufpa.facomp.veiculos.repository.AvaliacaoSolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.AvaliacaoSolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.AvaliacaoSolicitacaoMapper;
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
 * Service for executing complex queries for {@link AvaliacaoSolicitacao} entities in the database.
 * The main input is a {@link AvaliacaoSolicitacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AvaliacaoSolicitacaoDTO} or a {@link Page} of {@link AvaliacaoSolicitacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvaliacaoSolicitacaoQueryService extends QueryService<AvaliacaoSolicitacao> {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoSolicitacaoQueryService.class);

    private final AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository;

    private final AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper;

    public AvaliacaoSolicitacaoQueryService(
        AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository,
        AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper
    ) {
        this.avaliacaoSolicitacaoRepository = avaliacaoSolicitacaoRepository;
        this.avaliacaoSolicitacaoMapper = avaliacaoSolicitacaoMapper;
    }

    /**
     * Return a {@link List} of {@link AvaliacaoSolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AvaliacaoSolicitacaoDTO> findByCriteria(AvaliacaoSolicitacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AvaliacaoSolicitacao> specification = createSpecification(criteria);
        return avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AvaliacaoSolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AvaliacaoSolicitacaoDTO> findByCriteria(AvaliacaoSolicitacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AvaliacaoSolicitacao> specification = createSpecification(criteria);
        return avaliacaoSolicitacaoRepository.findAll(specification, page).map(avaliacaoSolicitacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvaliacaoSolicitacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AvaliacaoSolicitacao> specification = createSpecification(criteria);
        return avaliacaoSolicitacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link AvaliacaoSolicitacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AvaliacaoSolicitacao> createSpecification(AvaliacaoSolicitacaoCriteria criteria) {
        Specification<AvaliacaoSolicitacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AvaliacaoSolicitacao_.id));
            }
            if (criteria.getValorGasolina() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getValorGasolina(), AvaliacaoSolicitacao_.valorGasolina));
            }
            if (criteria.getTotalGasto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalGasto(), AvaliacaoSolicitacao_.totalGasto));
            }
            if (criteria.getStatusSolicitacao() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getStatusSolicitacao(), AvaliacaoSolicitacao_.statusSolicitacao));
            }
            if (criteria.getSolicitacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSolicitacaoId(),
                            root -> root.join(AvaliacaoSolicitacao_.solicitacao, JoinType.LEFT).get(Solicitacao_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
