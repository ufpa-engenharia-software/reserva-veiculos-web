package br.ufpa.facomp.veiculos.web.rest;

import br.ufpa.facomp.veiculos.repository.VeiculoRepository;
import br.ufpa.facomp.veiculos.service.VeiculoQueryService;
import br.ufpa.facomp.veiculos.service.VeiculoService;
import br.ufpa.facomp.veiculos.service.criteria.VeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
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
 * REST controller for managing {@link br.ufpa.facomp.veiculos.domain.Veiculo}.
 */
@RestController
@RequestMapping("/api")
public class VeiculoResource {

    private final Logger log = LoggerFactory.getLogger(VeiculoResource.class);

    private static final String ENTITY_NAME = "veiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VeiculoService veiculoService;

    private final VeiculoRepository veiculoRepository;

    private final VeiculoQueryService veiculoQueryService;

    public VeiculoResource(VeiculoService veiculoService, VeiculoRepository veiculoRepository, VeiculoQueryService veiculoQueryService) {
        this.veiculoService = veiculoService;
        this.veiculoRepository = veiculoRepository;
        this.veiculoQueryService = veiculoQueryService;
    }

    /**
     * {@code POST  /veiculos} : Create a new veiculo.
     *
     * @param veiculoDTO the veiculoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new veiculoDTO, or with status {@code 400 (Bad Request)} if the veiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/veiculos")
    public ResponseEntity<VeiculoDTO> createVeiculo(@Valid @RequestBody VeiculoDTO veiculoDTO) throws URISyntaxException {
        log.debug("REST request to save Veiculo : {}", veiculoDTO);
        if (veiculoDTO.getId() != null) {
            throw new BadRequestAlertException("A new veiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VeiculoDTO result = veiculoService.save(veiculoDTO);
        return ResponseEntity
            .created(new URI("/api/veiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /veiculos/:id} : Updates an existing veiculo.
     *
     * @param id the id of the veiculoDTO to save.
     * @param veiculoDTO the veiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veiculoDTO,
     * or with status {@code 400 (Bad Request)} if the veiculoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the veiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/veiculos/{id}")
    public ResponseEntity<VeiculoDTO> updateVeiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VeiculoDTO veiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Veiculo : {}, {}", id, veiculoDTO);
        if (veiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VeiculoDTO result = veiculoService.save(veiculoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, veiculoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /veiculos/:id} : Partial updates given fields of an existing veiculo, field will ignore if it is null
     *
     * @param id the id of the veiculoDTO to save.
     * @param veiculoDTO the veiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veiculoDTO,
     * or with status {@code 400 (Bad Request)} if the veiculoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the veiculoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the veiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/veiculos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VeiculoDTO> partialUpdateVeiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VeiculoDTO veiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Veiculo partially : {}, {}", id, veiculoDTO);
        if (veiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VeiculoDTO> result = veiculoService.partialUpdate(veiculoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, veiculoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /veiculos} : get all the veiculos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of veiculos in body.
     */
    @GetMapping("/veiculos")
    public ResponseEntity<List<VeiculoDTO>> getAllVeiculos(VeiculoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Veiculos by criteria: {}", criteria);
        Page<VeiculoDTO> page = veiculoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /veiculos/count} : count all the veiculos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/veiculos/count")
    public ResponseEntity<Long> countVeiculos(VeiculoCriteria criteria) {
        log.debug("REST request to count Veiculos by criteria: {}", criteria);
        return ResponseEntity.ok().body(veiculoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /veiculos/:id} : get the "id" veiculo.
     *
     * @param id the id of the veiculoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the veiculoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/veiculos/{id}")
    public ResponseEntity<VeiculoDTO> getVeiculo(@PathVariable Long id) {
        log.debug("REST request to get Veiculo : {}", id);
        Optional<VeiculoDTO> veiculoDTO = veiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(veiculoDTO);
    }

    /**
     * {@code DELETE  /veiculos/:id} : delete the "id" veiculo.
     *
     * @param id the id of the veiculoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/veiculos/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable Long id) {
        log.debug("REST request to delete Veiculo : {}", id);
        veiculoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
