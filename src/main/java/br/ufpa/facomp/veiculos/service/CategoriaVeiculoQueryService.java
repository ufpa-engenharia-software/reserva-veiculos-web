package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import br.ufpa.facomp.veiculos.repository.CategoriaVeiculoRepository;
import br.ufpa.facomp.veiculos.service.criteria.CategoriaVeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.CategoriaVeiculoMapper;
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
 * Service for executing complex queries for {@link CategoriaVeiculo} entities in the database.
 * The main input is a {@link CategoriaVeiculoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoriaVeiculoDTO} or a {@link Page} of {@link CategoriaVeiculoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaVeiculoQueryService extends QueryService<CategoriaVeiculo> {

    private final Logger log = LoggerFactory.getLogger(CategoriaVeiculoQueryService.class);

    private final CategoriaVeiculoRepository categoriaVeiculoRepository;

    private final CategoriaVeiculoMapper categoriaVeiculoMapper;

    public CategoriaVeiculoQueryService(
        CategoriaVeiculoRepository categoriaVeiculoRepository,
        CategoriaVeiculoMapper categoriaVeiculoMapper
    ) {
        this.categoriaVeiculoRepository = categoriaVeiculoRepository;
        this.categoriaVeiculoMapper = categoriaVeiculoMapper;
    }

    /**
     * Return a {@link List} of {@link CategoriaVeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriaVeiculoDTO> findByCriteria(CategoriaVeiculoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CategoriaVeiculo> specification = createSpecification(criteria);
        return categoriaVeiculoMapper.toDto(categoriaVeiculoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategoriaVeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaVeiculoDTO> findByCriteria(CategoriaVeiculoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoriaVeiculo> specification = createSpecification(criteria);
        return categoriaVeiculoRepository.findAll(specification, page).map(categoriaVeiculoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaVeiculoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CategoriaVeiculo> specification = createSpecification(criteria);
        return categoriaVeiculoRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaVeiculoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoriaVeiculo> createSpecification(CategoriaVeiculoCriteria criteria) {
        Specification<CategoriaVeiculo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoriaVeiculo_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), CategoriaVeiculo_.nome));
            }
            if (criteria.getCapacidadePessoas() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCapacidadePessoas(), CategoriaVeiculo_.capacidadePessoas));
            }
            if (criteria.getCapacidadePeso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacidadePeso(), CategoriaVeiculo_.capacidadePeso));
            }
            if (criteria.getCapacidadeArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacidadeArea(), CategoriaVeiculo_.capacidadeArea));
            }
            if (criteria.getEixos() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEixos(), CategoriaVeiculo_.eixos));
            }
            if (criteria.getAltura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAltura(), CategoriaVeiculo_.altura));
            }
            if (criteria.getNivelCNH() != null) {
                specification = specification.and(buildSpecification(criteria.getNivelCNH(), CategoriaVeiculo_.nivelCNH));
            }
        }
        return specification;
    }
}
