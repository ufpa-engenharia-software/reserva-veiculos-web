package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.ManutencaoRepository;
import br.ufpa.facomp.veiculos.service.ManutencaoQueryService;
import br.ufpa.facomp.veiculos.service.ManutencaoService;
import br.ufpa.facomp.veiculos.service.criteria.ManutencaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
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
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.Manutencao}.
 */
@RestController
@RequestMapping("/api")
public class ManutencaoResource {

    private final Logger log = LoggerFactory.getLogger(ManutencaoResource.class);

    private static final String ENTITY_NAME = "manutencao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManutencaoService manutencaoService;

    private final ManutencaoRepository manutencaoRepository;

    private final ManutencaoQueryService manutencaoQueryService;

    public ManutencaoResource(
        ManutencaoService manutencaoService,
        ManutencaoRepository manutencaoRepository,
        ManutencaoQueryService manutencaoQueryService
    ) {
        this.manutencaoService = manutencaoService;
        this.manutencaoRepository = manutencaoRepository;
        this.manutencaoQueryService = manutencaoQueryService;
    }

    /**
     * {@code POST  /manutencaos} : Create a new manutencao.
     *
     * @param manutencaoDTO the manutencaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manutencaoDTO, or with status {@code 400 (Bad Request)} if the manutencao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manutencaos")
    public ResponseEntity<ManutencaoDTO> createManutencao(@RequestBody ManutencaoDTO manutencaoDTO) throws URISyntaxException {
        log.debug("REST request to save Manutencao : {}", manutencaoDTO);
        if (manutencaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new manutencao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManutencaoDTO result = manutencaoService.save(manutencaoDTO);
        return ResponseEntity
            .created(new URI("/api/manutencaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manutencaos/:id} : Updates an existing manutencao.
     *
     * @param id the id of the manutencaoDTO to save.
     * @param manutencaoDTO the manutencaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manutencaoDTO,
     * or with status {@code 400 (Bad Request)} if the manutencaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manutencaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manutencaos/{id}")
    public ResponseEntity<ManutencaoDTO> updateManutencao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ManutencaoDTO manutencaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Manutencao : {}, {}", id, manutencaoDTO);
        if (manutencaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manutencaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manutencaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManutencaoDTO result = manutencaoService.save(manutencaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manutencaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /manutencaos/:id} : Partial updates given fields of an existing manutencao, field will ignore if it is null
     *
     * @param id the id of the manutencaoDTO to save.
     * @param manutencaoDTO the manutencaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manutencaoDTO,
     * or with status {@code 400 (Bad Request)} if the manutencaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manutencaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manutencaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manutencaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ManutencaoDTO> partialUpdateManutencao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ManutencaoDTO manutencaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Manutencao partially : {}, {}", id, manutencaoDTO);
        if (manutencaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manutencaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manutencaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManutencaoDTO> result = manutencaoService.partialUpdate(manutencaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manutencaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manutencaos} : get all the manutencaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manutencaos in body.
     */
    @GetMapping("/manutencaos")
    public ResponseEntity<List<ManutencaoDTO>> getAllManutencaos(ManutencaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Manutencaos by criteria: {}", criteria);
        Page<ManutencaoDTO> page = manutencaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manutencaos/count} : count all the manutencaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/manutencaos/count")
    public ResponseEntity<Long> countManutencaos(ManutencaoCriteria criteria) {
        log.debug("REST request to count Manutencaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(manutencaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /manutencaos/:id} : get the "id" manutencao.
     *
     * @param id the id of the manutencaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manutencaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manutencaos/{id}")
    public ResponseEntity<ManutencaoDTO> getManutencao(@PathVariable Long id) {
        log.debug("REST request to get Manutencao : {}", id);
        Optional<ManutencaoDTO> manutencaoDTO = manutencaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manutencaoDTO);
    }

    /**
     * {@code DELETE  /manutencaos/:id} : delete the "id" manutencao.
     *
     * @param id the id of the manutencaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manutencaos/{id}")
    public ResponseEntity<Void> deleteManutencao(@PathVariable Long id) {
        log.debug("REST request to delete Manutencao : {}", id);
        manutencaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
