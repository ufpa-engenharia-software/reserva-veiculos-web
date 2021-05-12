package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.repository.VeiculoRepository;
import br.ufpa.facomp.veiculos.service.criteria.VeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.VeiculoMapper;
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
 * Service for executing complex queries for {@link Veiculo} entities in the database.
 * The main input is a {@link VeiculoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VeiculoDTO} or a {@link Page} of {@link VeiculoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VeiculoQueryService extends QueryService<Veiculo> {

    private final Logger log = LoggerFactory.getLogger(VeiculoQueryService.class);

    private final VeiculoRepository veiculoRepository;

    private final VeiculoMapper veiculoMapper;

    public VeiculoQueryService(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
    }

    /**
     * Return a {@link List} of {@link VeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VeiculoDTO> findByCriteria(VeiculoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Veiculo> specification = createSpecification(criteria);
        return veiculoMapper.toDto(veiculoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VeiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VeiculoDTO> findByCriteria(VeiculoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Veiculo> specification = createSpecification(criteria);
        return veiculoRepository.findAll(specification, page).map(veiculoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VeiculoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Veiculo> specification = createSpecification(criteria);
        return veiculoRepository.count(specification);
    }

    /**
     * Function to convert {@link VeiculoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Veiculo> createSpecification(VeiculoCriteria criteria) {
        Specification<Veiculo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Veiculo_.id));
            }
            if (criteria.getPlaca() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaca(), Veiculo_.placa));
            }
            if (criteria.getModelo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelo(), Veiculo_.modelo));
            }
            if (criteria.getAno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAno(), Veiculo_.ano));
            }
            if (criteria.getDisponivel() != null) {
                specification = specification.and(buildSpecification(criteria.getDisponivel(), Veiculo_.disponivel));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Veiculo_.criado));
            }
            if (criteria.getFabricanteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFabricanteId(),
                            root -> root.join(Veiculo_.fabricante, JoinType.LEFT).get(Fabricante_.id)
                        )
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Veiculo_.categoria, JoinType.LEFT).get(CategoriaVeiculo_.id)
                        )
                    );
            }
            if (criteria.getMotoristasHabilitadosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMotoristasHabilitadosId(),
                            root -> root.join(Veiculo_.motoristasHabilitados, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
