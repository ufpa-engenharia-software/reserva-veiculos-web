package br.ufpa.facomp.veiculos.web.rest;

import static br.ufpa.facomp.veiculos.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.Fabricante;
import br.ufpa.facomp.veiculos.repository.FabricanteRepository;
import br.ufpa.facomp.veiculos.service.criteria.FabricanteCriteria;
import br.ufpa.facomp.veiculos.service.dto.FabricanteDTO;
import br.ufpa.facomp.veiculos.service.mapper.FabricanteMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FabricanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FabricanteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/fabricantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FabricanteRepository fabricanteRepository;

    @Autowired
    private FabricanteMapper fabricanteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFabricanteMockMvc;

    private Fabricante fabricante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricante createEntity(EntityManager em) {
        Fabricante fabricante = new Fabricante().nome(DEFAULT_NOME).criado(DEFAULT_CRIADO);
        return fabricante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fabricante createUpdatedEntity(EntityManager em) {
        Fabricante fabricante = new Fabricante().nome(UPDATED_NOME).criado(UPDATED_CRIADO);
        return fabricante;
    }

    @BeforeEach
    public void initTest() {
        fabricante = createEntity(em);
    }

    @Test
    @Transactional
    void createFabricante() throws Exception {
        int databaseSizeBeforeCreate = fabricanteRepository.findAll().size();
        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);
        restFabricanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isCreated());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeCreate + 1);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createFabricanteWithExistingId() throws Exception {
        // Create the Fabricante with an existing ID
        fabricante.setId(1L);
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        int databaseSizeBeforeCreate = fabricanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFabricanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricanteRepository.findAll().size();
        // set the field null
        fabricante.setNome(null);

        // Create the Fabricante, which fails.
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        restFabricanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isBadRequest());

        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFabricantes() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get the fabricante
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL_ID, fabricante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fabricante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getFabricantesByIdFiltering() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        Long id = fabricante.getId();

        defaultFabricanteShouldBeFound("id.equals=" + id);
        defaultFabricanteShouldNotBeFound("id.notEquals=" + id);

        defaultFabricanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFabricanteShouldNotBeFound("id.greaterThan=" + id);

        defaultFabricanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFabricanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome equals to DEFAULT_NOME
        defaultFabricanteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome equals to UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome not equals to DEFAULT_NOME
        defaultFabricanteShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome not equals to UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the fabricanteList where nome equals to UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome is not null
        defaultFabricanteShouldBeFound("nome.specified=true");

        // Get all the fabricanteList where nome is null
        defaultFabricanteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeContainsSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome contains DEFAULT_NOME
        defaultFabricanteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome contains UPDATED_NOME
        defaultFabricanteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFabricantesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where nome does not contain DEFAULT_NOME
        defaultFabricanteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the fabricanteList where nome does not contain UPDATED_NOME
        defaultFabricanteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado equals to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado equals to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado not equals to DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado not equals to UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the fabricanteList where criado equals to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is not null
        defaultFabricanteShouldBeFound("criado.specified=true");

        // Get all the fabricanteList where criado is null
        defaultFabricanteShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is greater than or equal to UPDATED_CRIADO
        defaultFabricanteShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is less than or equal to DEFAULT_CRIADO
        defaultFabricanteShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is less than or equal to SMALLER_CRIADO
        defaultFabricanteShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is less than DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is less than UPDATED_CRIADO
        defaultFabricanteShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFabricantesByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        // Get all the fabricanteList where criado is greater than DEFAULT_CRIADO
        defaultFabricanteShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the fabricanteList where criado is greater than SMALLER_CRIADO
        defaultFabricanteShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFabricanteShouldBeFound(String filter) throws Exception {
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFabricanteShouldNotBeFound(String filter) throws Exception {
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFabricanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFabricante() throws Exception {
        // Get the fabricante
        restFabricanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();

        // Update the fabricante
        Fabricante updatedFabricante = fabricanteRepository.findById(fabricante.getId()).get();
        // Disconnect from session so that the updates on updatedFabricante are not directly saved in db
        em.detach(updatedFabricante);
        updatedFabricante.nome(UPDATED_NOME).criado(UPDATED_CRIADO);
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(updatedFabricante);

        restFabricanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fabricanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fabricanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fabricanteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFabricanteWithPatch() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();

        // Update the fabricante using partial update
        Fabricante partialUpdatedFabricante = new Fabricante();
        partialUpdatedFabricante.setId(fabricante.getId());

        restFabricanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFabricante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFabricante))
            )
            .andExpect(status().isOk());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateFabricanteWithPatch() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();

        // Update the fabricante using partial update
        Fabricante partialUpdatedFabricante = new Fabricante();
        partialUpdatedFabricante.setId(fabricante.getId());

        partialUpdatedFabricante.nome(UPDATED_NOME).criado(UPDATED_CRIADO);

        restFabricanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFabricante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFabricante))
            )
            .andExpect(status().isOk());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
        Fabricante testFabricante = fabricanteList.get(fabricanteList.size() - 1);
        assertThat(testFabricante.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFabricante.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fabricanteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFabricante() throws Exception {
        int databaseSizeBeforeUpdate = fabricanteRepository.findAll().size();
        fabricante.setId(count.incrementAndGet());

        // Create the Fabricante
        FabricanteDTO fabricanteDTO = fabricanteMapper.toDto(fabricante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFabricanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fabricanteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fabricante in the database
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFabricante() throws Exception {
        // Initialize the database
        fabricanteRepository.saveAndFlush(fabricante);

        int databaseSizeBeforeDelete = fabricanteRepository.findAll().size();

        // Delete the fabricante
        restFabricanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, fabricante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fabricante> fabricanteList = fabricanteRepository.findAll();
        assertThat(fabricanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
