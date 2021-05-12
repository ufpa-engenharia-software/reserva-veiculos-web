package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.SolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.SolicitacaoQueryService;
import br.ufpa.facomp.veiculos.service.SolicitacaoService;
import br.ufpa.facomp.veiculos.service.criteria.SolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import br.ufpa.facomp.veiculos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.Solicitacao}.
 */
@RestController
@RequestMapping("/api")
public class SolicitacaoResource {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoResource.class);

    private static final String ENTITY_NAME = "solicitacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolicitacaoService solicitacaoService;

    private final SolicitacaoRepository solicitacaoRepository;

    private final SolicitacaoQueryService solicitacaoQueryService;

    public SolicitacaoResource(
        SolicitacaoService solicitacaoService,
        SolicitacaoRepository solicitacaoRepository,
        SolicitacaoQueryService solicitacaoQueryService
    ) {
        this.solicitacaoService = solicitacaoService;
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoQueryService = solicitacaoQueryService;
    }

    /**
     * {@code POST  /solicitacaos} : Create a new solicitacao.
     *
     * @param solicitacaoDTO the solicitacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solicitacaoDTO, or with status {@code 400 (Bad Request)} if the solicitacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solicitacaos")
    public ResponseEntity<SolicitacaoDTO> createSolicitacao(@RequestBody SolicitacaoDTO solicitacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Solicitacao : {}", solicitacaoDTO);
        if (solicitacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new solicitacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolicitacaoDTO result = solicitacaoService.save(solicitacaoDTO);
        return ResponseEntity
            .created(new URI("/api/solicitacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solicitacaos/:id} : Updates an existing solicitacao.
     *
     * @param id the id of the solicitacaoDTO to save.
     * @param solicitacaoDTO the solicitacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solicitacaos/{id}")
    public ResponseEntity<SolicitacaoDTO> updateSolicitacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolicitacaoDTO solicitacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Solicitacao : {}, {}", id, solicitacaoDTO);
        if (solicitacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SolicitacaoDTO result = solicitacaoService.save(solicitacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solicitacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /solicitacaos/:id} : Partial updates given fields of an existing solicitacao, field will ignore if it is null
     *
     * @param id the id of the solicitacaoDTO to save.
     * @param solicitacaoDTO the solicitacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the solicitacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/solicitacaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SolicitacaoDTO> partialUpdateSolicitacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolicitacaoDTO solicitacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Solicitacao partially : {}, {}", id, solicitacaoDTO);
        if (solicitacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SolicitacaoDTO> result = solicitacaoService.partialUpdate(solicitacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solicitacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /solicitacaos} : get all the solicitacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solicitacaos in body.
     */
    @GetMapping("/solicitacaos")
    public ResponseEntity<List<SolicitacaoDTO>> getAllSolicitacaos(SolicitacaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Solicitacaos by criteria: {}", criteria);
        Page<SolicitacaoDTO> page = solicitacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /solicitacaos/count} : count all the solicitacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/solicitacaos/count")
    public ResponseEntity<Long> countSolicitacaos(SolicitacaoCriteria criteria) {
        log.debug("REST request to count Solicitacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(solicitacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /solicitacaos/:id} : get the "id" solicitacao.
     *
     * @param id the id of the solicitacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solicitacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solicitacaos/{id}")
    public ResponseEntity<SolicitacaoDTO> getSolicitacao(@PathVariable Long id) {
        log.debug("REST request to get Solicitacao : {}", id);
        Optional<SolicitacaoDTO> solicitacaoDTO = solicitacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solicitacaoDTO);
    }

    /**
     * {@code DELETE  /solicitacaos/:id} : delete the "id" solicitacao.
     *
     * @param id the id of the solicitacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solicitacaos/{id}")
    public ResponseEntity<Void> deleteSolicitacao(@PathVariable Long id) {
        log.debug("REST request to delete Solicitacao : {}", id);
        solicitacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
