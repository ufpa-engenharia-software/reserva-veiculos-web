package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.Fabricante;
import br.ufpa.facomp.veiculos.repository.FabricanteRepository;
import br.ufpa.facomp.veiculos.service.criteria.FabricanteCriteria;
import br.ufpa.facomp.veiculos.service.dto.FabricanteDTO;
import br.ufpa.facomp.veiculos.service.mapper.FabricanteMapper;
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
 * Service for executing complex queries for {@link Fabricante} entities in the database.
 * The main input is a {@link FabricanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FabricanteDTO} or a {@link Page} of {@link FabricanteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FabricanteQueryService extends QueryService<Fabricante> {

    private final Logger log = LoggerFactory.getLogger(FabricanteQueryService.class);

    private final FabricanteRepository fabricanteRepository;

    private final FabricanteMapper fabricanteMapper;

    public FabricanteQueryService(FabricanteRepository fabricanteRepository, FabricanteMapper fabricanteMapper) {
        this.fabricanteRepository = fabricanteRepository;
        this.fabricanteMapper = fabricanteMapper;
    }

    /**
     * Return a {@link List} of {@link FabricanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FabricanteDTO> findByCriteria(FabricanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fabricante> specification = createSpecification(criteria);
        return fabricanteMapper.toDto(fabricanteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FabricanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FabricanteDTO> findByCriteria(FabricanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fabricante> specification = createSpecification(criteria);
        return fabricanteRepository.findAll(specification, page).map(fabricanteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FabricanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fabricante> specification = createSpecification(criteria);
        return fabricanteRepository.count(specification);
    }

    /**
     * Function to convert {@link FabricanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fabricante> createSpecification(FabricanteCriteria criteria) {
        Specification<Fabricante> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fabricante_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Fabricante_.nome));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Fabricante_.criado));
            }
        }
        return specification;
    }
}
