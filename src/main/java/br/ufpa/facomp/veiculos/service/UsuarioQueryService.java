package br.ufpa.facomp.veiculos.service;

import br.ufpa.facomp.veiculos.domain.*; // for static metamodels
import br.ufpa.facomp.veiculos.domain.Usuario;
import br.ufpa.facomp.veiculos.repository.UsuarioRepository;
import br.ufpa.facomp.veiculos.service.criteria.UsuarioCriteria;
import br.ufpa.facomp.veiculos.service.dto.UsuarioDTO;
import br.ufpa.facomp.veiculos.service.mapper.UsuarioMapper;
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
 * Service for executing complex queries for {@link Usuario} entities in the database.
 * The main input is a {@link UsuarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioDTO} or a {@link Page} of {@link UsuarioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioQueryService extends QueryService<Usuario> {

    private final Logger log = LoggerFactory.getLogger(UsuarioQueryService.class);

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public UsuarioQueryService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * Return a {@link List} of {@link UsuarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioMapper.toDto(usuarioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsuarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findByCriteria(UsuarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.findAll(specification, page).map(usuarioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Usuario> createSpecification(UsuarioCriteria criteria) {
        Specification<Usuario> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Usuario_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Usuario_.nome));
            }
            if (criteria.getPerfil() != null) {
                specification = specification.and(buildSpecification(criteria.getPerfil(), Usuario_.perfil));
            }
            if (criteria.getIdentificacao() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentificacao(), Usuario_.identificacao));
            }
            if (criteria.getNidentificao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNidentificao(), Usuario_.nidentificao));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Usuario_.cpf));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Usuario_.email));
            }
            if (criteria.getCelular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelular(), Usuario_.celular));
            }
            if (criteria.getWhatsapp() != null) {
                specification = specification.and(buildSpecification(criteria.getWhatsapp(), Usuario_.whatsapp));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), Usuario_.ativo));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Usuario_.criado));
            }
            if (criteria.getNivelCNH() != null) {
                specification = specification.and(buildSpecification(criteria.getNivelCNH(), Usuario_.nivelCNH));
            }
            if (criteria.getMinhasSolicitacoesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMinhasSolicitacoesId(),
                            root -> root.join(Usuario_.minhasSolicitacoes, JoinType.LEFT).get(Solicitacao_.id)
                        )
                    );
            }
            if (criteria.getComoAutorizadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComoAutorizadorId(),
                            root -> root.join(Usuario_.comoAutorizadors, JoinType.LEFT).get(Solicitacao_.id)
                        )
                    );
            }
            if (criteria.getComoMotoristaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComoMotoristaId(),
                            root -> root.join(Usuario_.comoMotoristas, JoinType.LEFT).get(Solicitacao_.id)
                        )
                    );
            }
            if (criteria.getVeiculosHabilitadosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVeiculosHabilitadosId(),
                            root -> root.join(Usuario_.veiculosHabilitados, JoinType.LEFT).get(Veiculo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
