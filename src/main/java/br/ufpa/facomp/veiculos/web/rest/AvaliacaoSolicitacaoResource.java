package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.AvaliacaoSolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.AvaliacaoSolicitacaoQueryService;
import br.ufpa.facomp.veiculos.service.AvaliacaoSolicitacaoService;
import br.ufpa.facomp.veiculos.service.criteria.AvaliacaoSolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import br.ufpa.facomp.veiculos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao}.
 */
@RestController
@RequestMapping("/api")
public class AvaliacaoSolicitacaoResource {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoSolicitacaoResource.class);

    private static final String ENTITY_NAME = "avaliacaoSolicitacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvaliacaoSolicitacaoService avaliacaoSolicitacaoService;

    private final AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository;

    private final AvaliacaoSolicitacaoQueryService avaliacaoSolicitacaoQueryService;

    public AvaliacaoSolicitacaoResource(
        AvaliacaoSolicitacaoService avaliacaoSolicitacaoService,
        AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository,
        AvaliacaoSolicitacaoQueryService avaliacaoSolicitacaoQueryService
    ) {
        this.avaliacaoSolicitacaoService = avaliacaoSolicitacaoService;
        this.avaliacaoSolicitacaoRepository = avaliacaoSolicitacaoRepository;
        this.avaliacaoSolicitacaoQueryService = avaliacaoSolicitacaoQueryService;
    }

    /**
     * {@code POST  /avaliacao-solicitacaos} : Create a new avaliacaoSolicitacao.
     *
     * @param avaliacaoSolicitacaoDTO the avaliacaoSolicitacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avaliacaoSolicitacaoDTO, or with status {@code 400 (Bad Request)} if the avaliacaoSolicitacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avaliacao-solicitacaos")
    public ResponseEntity<AvaliacaoSolicitacaoDTO> createAvaliacaoSolicitacao(@RequestBody AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO)
        throws URISyntaxException {
        log.debug("REST request to save AvaliacaoSolicitacao : {}", avaliacaoSolicitacaoDTO);
        if (avaliacaoSolicitacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new avaliacaoSolicitacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AvaliacaoSolicitacaoDTO result = avaliacaoSolicitacaoService.save(avaliacaoSolicitacaoDTO);
        return ResponseEntity
            .created(new URI("/api/avaliacao-solicitacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /avaliacao-solicitacaos/:id} : Updates an existing avaliacaoSolicitacao.
     *
     * @param id the id of the avaliacaoSolicitacaoDTO to save.
     * @param avaliacaoSolicitacaoDTO the avaliacaoSolicitacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacaoSolicitacaoDTO,
     * or with status {@code 400 (Bad Request)} if the avaliacaoSolicitacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avaliacaoSolicitacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avaliacao-solicitacaos/{id}")
    public ResponseEntity<AvaliacaoSolicitacaoDTO> updateAvaliacaoSolicitacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AvaliacaoSolicitacao : {}, {}", id, avaliacaoSolicitacaoDTO);
        if (avaliacaoSolicitacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avaliacaoSolicitacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avaliacaoSolicitacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AvaliacaoSolicitacaoDTO result = avaliacaoSolicitacaoService.save(avaliacaoSolicitacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avaliacaoSolicitacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /avaliacao-solicitacaos/:id} : Partial updates given fields of an existing avaliacaoSolicitacao, field will ignore if it is null
     *
     * @param id the id of the avaliacaoSolicitacaoDTO to save.
     * @param avaliacaoSolicitacaoDTO the avaliacaoSolicitacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacaoSolicitacaoDTO,
     * or with status {@code 400 (Bad Request)} if the avaliacaoSolicitacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the avaliacaoSolicitacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the avaliacaoSolicitacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/avaliacao-solicitacaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AvaliacaoSolicitacaoDTO> partialUpdateAvaliacaoSolicitacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AvaliacaoSolicitacao partially : {}, {}", id, avaliacaoSolicitacaoDTO);
        if (avaliacaoSolicitacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avaliacaoSolicitacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avaliacaoSolicitacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvaliacaoSolicitacaoDTO> result = avaliacaoSolicitacaoService.partialUpdate(avaliacaoSolicitacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avaliacaoSolicitacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /avaliacao-solicitacaos} : get all the avaliacaoSolicitacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avaliacaoSolicitacaos in body.
     */
    @GetMapping("/avaliacao-solicitacaos")
    public ResponseEntity<List<AvaliacaoSolicitacaoDTO>> getAllAvaliacaoSolicitacaos(
        AvaliacaoSolicitacaoCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AvaliacaoSolicitacaos by criteria: {}", criteria);
        Page<AvaliacaoSolicitacaoDTO> page = avaliacaoSolicitacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avaliacao-solicitacaos/count} : count all the avaliacaoSolicitacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/avaliacao-solicitacaos/count")
    public ResponseEntity<Long> countAvaliacaoSolicitacaos(AvaliacaoSolicitacaoCriteria criteria) {
        log.debug("REST request to count AvaliacaoSolicitacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(avaliacaoSolicitacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /avaliacao-solicitacaos/:id} : get the "id" avaliacaoSolicitacao.
     *
     * @param id the id of the avaliacaoSolicitacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avaliacaoSolicitacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avaliacao-solicitacaos/{id}")
    public ResponseEntity<AvaliacaoSolicitacaoDTO> getAvaliacaoSolicitacao(@PathVariable Long id) {
        log.debug("REST request to get AvaliacaoSolicitacao : {}", id);
        Optional<AvaliacaoSolicitacaoDTO> avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avaliacaoSolicitacaoDTO);
    }

    /**
     * {@code DELETE  /avaliacao-solicitacaos/:id} : delete the "id" avaliacaoSolicitacao.
     *
     * @param id the id of the avaliacaoSolicitacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avaliacao-solicitacaos/{id}")
    public ResponseEntity<Void> deleteAvaliacaoSolicitacao(@PathVariable Long id) {
        log.debug("REST request to delete AvaliacaoSolicitacao : {}", id);
        avaliacaoSolicitacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
