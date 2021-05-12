package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.FabricanteRepository;
import br.ufpa.facomp.veiculos.service.FabricanteQueryService;
import br.ufpa.facomp.veiculos.service.FabricanteService;
import br.ufpa.facomp.veiculos.service.criteria.FabricanteCriteria;
import br.ufpa.facomp.veiculos.service.dto.FabricanteDTO;
import br.ufpa.facomp.veiculos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.Fabricante}.
 */
@RestController
@RequestMapping("/api")
public class FabricanteResource {

    private final Logger log = LoggerFactory.getLogger(FabricanteResource.class);

    private static final String ENTITY_NAME = "fabricante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FabricanteService fabricanteService;

    private final FabricanteRepository fabricanteRepository;

    private final FabricanteQueryService fabricanteQueryService;

    public FabricanteResource(
        FabricanteService fabricanteService,
        FabricanteRepository fabricanteRepository,
        FabricanteQueryService fabricanteQueryService
    ) {
        this.fabricanteService = fabricanteService;
        this.fabricanteRepository = fabricanteRepository;
        this.fabricanteQueryService = fabricanteQueryService;
    }

    /**
     * {@code POST  /fabricantes} : Create a new fabricante.
     *
     * @param fabricanteDTO the fabricanteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fabricanteDTO, or with status {@code 400 (Bad Request)} if the fabricante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fabricantes")
    public ResponseEntity<FabricanteDTO> createFabricante(@Valid @RequestBody FabricanteDTO fabricanteDTO) throws URISyntaxException {
        log.debug("REST request to save Fabricante : {}", fabricanteDTO);
        if (fabricanteDTO.getId() != null) {
            throw new BadRequestAlertException("A new fabricante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FabricanteDTO result = fabricanteService.save(fabricanteDTO);
        return ResponseEntity
            .created(new URI("/api/fabricantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fabricantes/:id} : Updates an existing fabricante.
     *
     * @param id the id of the fabricanteDTO to save.
     * @param fabricanteDTO the fabricanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricanteDTO,
     * or with status {@code 400 (Bad Request)} if the fabricanteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fabricanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fabricantes/{id}")
    public ResponseEntity<FabricanteDTO> updateFabricante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FabricanteDTO fabricanteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fabricante : {}, {}", id, fabricanteDTO);
        if (fabricanteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fabricanteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fabricanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FabricanteDTO result = fabricanteService.save(fabricanteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fabricanteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fabricantes/:id} : Partial updates given fields of an existing fabricante, field will ignore if it is null
     *
     * @param id the id of the fabricanteDTO to save.
     * @param fabricanteDTO the fabricanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fabricanteDTO,
     * or with status {@code 400 (Bad Request)} if the fabricanteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fabricanteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fabricanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fabricantes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FabricanteDTO> partialUpdateFabricante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FabricanteDTO fabricanteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fabricante partially : {}, {}", id, fabricanteDTO);
        if (fabricanteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fabricanteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fabricanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FabricanteDTO> result = fabricanteService.partialUpdate(fabricanteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fabricanteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fabricantes} : get all the fabricantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fabricantes in body.
     */
    @GetMapping("/fabricantes")
    public ResponseEntity<List<FabricanteDTO>> getAllFabricantes(FabricanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Fabricantes by criteria: {}", criteria);
        Page<FabricanteDTO> page = fabricanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fabricantes/count} : count all the fabricantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fabricantes/count")
    public ResponseEntity<Long> countFabricantes(FabricanteCriteria criteria) {
        log.debug("REST request to count Fabricantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fabricanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fabricantes/:id} : get the "id" fabricante.
     *
     * @param id the id of the fabricanteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fabricanteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fabricantes/{id}")
    public ResponseEntity<FabricanteDTO> getFabricante(@PathVariable Long id) {
        log.debug("REST request to get Fabricante : {}", id);
        Optional<FabricanteDTO> fabricanteDTO = fabricanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fabricanteDTO);
    }

    /**
     * {@code DELETE  /fabricantes/:id} : delete the "id" fabricante.
     *
     * @param id the id of the fabricanteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fabricantes/{id}")
    public ResponseEntity<Void> deleteFabricante(@PathVariable Long id) {
        log.debug("REST request to delete Fabricante : {}", id);
        fabricanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
