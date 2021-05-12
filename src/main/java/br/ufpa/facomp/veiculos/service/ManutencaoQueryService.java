package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.Manutencao;
import br.ufpa.facomp.veiculos.repository.ManutencaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.ManutencaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.ManutencaoMapper;
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
 * Service for executing complex queries for {@link Manutencao} entities in the database.
 * The main input is a {@link ManutencaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ManutencaoDTO} or a {@link Page} of {@link ManutencaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ManutencaoQueryService extends QueryService<Manutencao> {

    private final Logger log = LoggerFactory.getLogger(ManutencaoQueryService.class);

    private final ManutencaoRepository manutencaoRepository;

    private final ManutencaoMapper manutencaoMapper;

    public ManutencaoQueryService(ManutencaoRepository manutencaoRepository, ManutencaoMapper manutencaoMapper) {
        this.manutencaoRepository = manutencaoRepository;
        this.manutencaoMapper = manutencaoMapper;
    }

    /**
     * Return a {@link List} of {@link ManutencaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ManutencaoDTO> findByCriteria(ManutencaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Manutencao> specification = createSpecification(criteria);
        return manutencaoMapper.toDto(manutencaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ManutencaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ManutencaoDTO> findByCriteria(ManutencaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Manutencao> specification = createSpecification(criteria);
        return manutencaoRepository.findAll(specification, page).map(manutencaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ManutencaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Manutencao> specification = createSpecification(criteria);
        return manutencaoRepository.count(specification);
    }

    /**
     * Function to convert {@link ManutencaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Manutencao> createSpecification(ManutencaoCriteria criteria) {
        Specification<Manutencao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Manutencao_.id));
            }
            if (criteria.getKm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKm(), Manutencao_.km));
            }
            if (criteria.getCusto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCusto(), Manutencao_.custo));
            }
            if (criteria.getVeiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVeiculoId(), root -> root.join(Manutencao_.veiculo, JoinType.LEFT).get(Veiculo_.id))
                    );
            }
        }
        return specification;
    }
}
