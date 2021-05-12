package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.CategoriaVeiculoRepository;
import br.ufpa.facomp.veiculos.service.CategoriaVeiculoQueryService;
import br.ufpa.facomp.veiculos.service.CategoriaVeiculoService;
import br.ufpa.facomp.veiculos.service.criteria.CategoriaVeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
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
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.CategoriaVeiculo}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaVeiculoResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaVeiculoResource.class);

    private static final String ENTITY_NAME = "categoriaVeiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaVeiculoService categoriaVeiculoService;

    private final CategoriaVeiculoRepository categoriaVeiculoRepository;

    private final CategoriaVeiculoQueryService categoriaVeiculoQueryService;

    public CategoriaVeiculoResource(
        CategoriaVeiculoService categoriaVeiculoService,
        CategoriaVeiculoRepository categoriaVeiculoRepository,
        CategoriaVeiculoQueryService categoriaVeiculoQueryService
    ) {
        this.categoriaVeiculoService = categoriaVeiculoService;
        this.categoriaVeiculoRepository = categoriaVeiculoRepository;
        this.categoriaVeiculoQueryService = categoriaVeiculoQueryService;
    }

    /**
     * {@code POST  /categoria-veiculos} : Create a new categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the categoriaVeiculoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaVeiculoDTO, or with status {@code 400 (Bad Request)} if the categoriaVeiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categoria-veiculos")
    public ResponseEntity<CategoriaVeiculoDTO> createCategoriaVeiculo(@RequestBody CategoriaVeiculoDTO categoriaVeiculoDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoriaVeiculo : {}", categoriaVeiculoDTO);
        if (categoriaVeiculoDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaVeiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoriaVeiculoDTO result = categoriaVeiculoService.save(categoriaVeiculoDTO);
        return ResponseEntity
            .created(new URI("/api/categoria-veiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categoria-veiculos/:id} : Updates an existing categoriaVeiculo.
     *
     * @param id the id of the categoriaVeiculoDTO to save.
     * @param categoriaVeiculoDTO the categoriaVeiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaVeiculoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaVeiculoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaVeiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categoria-veiculos/{id}")
    public ResponseEntity<CategoriaVeiculoDTO> updateCategoriaVeiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaVeiculoDTO categoriaVeiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoriaVeiculo : {}, {}", id, categoriaVeiculoDTO);
        if (categoriaVeiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaVeiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoriaVeiculoDTO result = categoriaVeiculoService.save(categoriaVeiculoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaVeiculoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categoria-veiculos/:id} : Partial updates given fields of an existing categoriaVeiculo, field will ignore if it is null
     *
     * @param id the id of the categoriaVeiculoDTO to save.
     * @param categoriaVeiculoDTO the categoriaVeiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaVeiculoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaVeiculoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaVeiculoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaVeiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categoria-veiculos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CategoriaVeiculoDTO> partialUpdateCategoriaVeiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaVeiculoDTO categoriaVeiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoriaVeiculo partially : {}, {}", id, categoriaVeiculoDTO);
        if (categoriaVeiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaVeiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaVeiculoDTO> result = categoriaVeiculoService.partialUpdate(categoriaVeiculoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaVeiculoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categoria-veiculos} : get all the categoriaVeiculos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoriaVeiculos in body.
     */
    @GetMapping("/categoria-veiculos")
    public ResponseEntity<List<CategoriaVeiculoDTO>> getAllCategoriaVeiculos(CategoriaVeiculoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CategoriaVeiculos by criteria: {}", criteria);
        Page<CategoriaVeiculoDTO> page = categoriaVeiculoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categoria-veiculos/count} : count all the categoriaVeiculos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categoria-veiculos/count")
    public ResponseEntity<Long> countCategoriaVeiculos(CategoriaVeiculoCriteria criteria) {
        log.debug("REST request to count CategoriaVeiculos by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoriaVeiculoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categoria-veiculos/:id} : get the "id" categoriaVeiculo.
     *
     * @param id the id of the categoriaVeiculoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaVeiculoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categoria-veiculos/{id}")
    public ResponseEntity<CategoriaVeiculoDTO> getCategoriaVeiculo(@PathVariable Long id) {
        log.debug("REST request to get CategoriaVeiculo : {}", id);
        Optional<CategoriaVeiculoDTO> categoriaVeiculoDTO = categoriaVeiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaVeiculoDTO);
    }

    /**
     * {@code DELETE  /categoria-veiculos/:id} : delete the "id" categoriaVeiculo.
     *
     * @param id the id of the categoriaVeiculoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categoria-veiculos/{id}")
    public ResponseEntity<Void> deleteCategoriaVeiculo(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaVeiculo : {}", id);
        categoriaVeiculoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
