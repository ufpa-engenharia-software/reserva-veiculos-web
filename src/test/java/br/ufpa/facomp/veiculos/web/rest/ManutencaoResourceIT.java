package br.ufpa.facomp.veiculos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.Manutencao;
import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.repository.ManutencaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.ManutencaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.ManutencaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.ManutencaoMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ManutencaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManutencaoResourceIT {

    private static final Double DEFAULT_KM = 1D;
    private static final Double UPDATED_KM = 2D;
    private static final Double SMALLER_KM = 1D - 1D;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Double DEFAULT_CUSTO = 1D;
    private static final Double UPDATED_CUSTO = 2D;
    private static final Double SMALLER_CUSTO = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/manutencaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private ManutencaoMapper manutencaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManutencaoMockMvc;

    private Manutencao manutencao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manutencao createEntity(EntityManager em) {
        Manutencao manutencao = new Manutencao().km(DEFAULT_KM).descricao(DEFAULT_DESCRICAO).custo(DEFAULT_CUSTO);
        return manutencao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manutencao createUpdatedEntity(EntityManager em) {
        Manutencao manutencao = new Manutencao().km(UPDATED_KM).descricao(UPDATED_DESCRICAO).custo(UPDATED_CUSTO);
        return manutencao;
    }

    @BeforeEach
    public void initTest() {
        manutencao = createEntity(em);
    }

    @Test
    @Transactional
    void createManutencao() throws Exception {
        int databaseSizeBeforeCreate = manutencaoRepository.findAll().size();
        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);
        restManutencaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manutencaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeCreate + 1);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getKm()).isEqualTo(DEFAULT_KM);
        assertThat(testManutencao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testManutencao.getCusto()).isEqualTo(DEFAULT_CUSTO);
    }

    @Test
    @Transactional
    void createManutencaoWithExistingId() throws Exception {
        // Create the Manutencao with an existing ID
        manutencao.setId(1L);
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        int databaseSizeBeforeCreate = manutencaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManutencaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manutencaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManutencaos() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manutencao.getId().intValue())))
            .andExpect(jsonPath("$.[*].km").value(hasItem(DEFAULT_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].custo").value(hasItem(DEFAULT_CUSTO.doubleValue())));
    }

    @Test
    @Transactional
    void getManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get the manutencao
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL_ID, manutencao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manutencao.getId().intValue()))
            .andExpect(jsonPath("$.km").value(DEFAULT_KM.doubleValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.custo").value(DEFAULT_CUSTO.doubleValue()));
    }

    @Test
    @Transactional
    void getManutencaosByIdFiltering() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        Long id = manutencao.getId();

        defaultManutencaoShouldBeFound("id.equals=" + id);
        defaultManutencaoShouldNotBeFound("id.notEquals=" + id);

        defaultManutencaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManutencaoShouldNotBeFound("id.greaterThan=" + id);

        defaultManutencaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManutencaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km equals to DEFAULT_KM
        defaultManutencaoShouldBeFound("km.equals=" + DEFAULT_KM);

        // Get all the manutencaoList where km equals to UPDATED_KM
        defaultManutencaoShouldNotBeFound("km.equals=" + UPDATED_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km not equals to DEFAULT_KM
        defaultManutencaoShouldNotBeFound("km.notEquals=" + DEFAULT_KM);

        // Get all the manutencaoList where km not equals to UPDATED_KM
        defaultManutencaoShouldBeFound("km.notEquals=" + UPDATED_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsInShouldWork() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km in DEFAULT_KM or UPDATED_KM
        defaultManutencaoShouldBeFound("km.in=" + DEFAULT_KM + "," + UPDATED_KM);

        // Get all the manutencaoList where km equals to UPDATED_KM
        defaultManutencaoShouldNotBeFound("km.in=" + UPDATED_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km is not null
        defaultManutencaoShouldBeFound("km.specified=true");

        // Get all the manutencaoList where km is null
        defaultManutencaoShouldNotBeFound("km.specified=false");
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km is greater than or equal to DEFAULT_KM
        defaultManutencaoShouldBeFound("km.greaterThanOrEqual=" + DEFAULT_KM);

        // Get all the manutencaoList where km is greater than or equal to UPDATED_KM
        defaultManutencaoShouldNotBeFound("km.greaterThanOrEqual=" + UPDATED_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km is less than or equal to DEFAULT_KM
        defaultManutencaoShouldBeFound("km.lessThanOrEqual=" + DEFAULT_KM);

        // Get all the manutencaoList where km is less than or equal to SMALLER_KM
        defaultManutencaoShouldNotBeFound("km.lessThanOrEqual=" + SMALLER_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsLessThanSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km is less than DEFAULT_KM
        defaultManutencaoShouldNotBeFound("km.lessThan=" + DEFAULT_KM);

        // Get all the manutencaoList where km is less than UPDATED_KM
        defaultManutencaoShouldBeFound("km.lessThan=" + UPDATED_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where km is greater than DEFAULT_KM
        defaultManutencaoShouldNotBeFound("km.greaterThan=" + DEFAULT_KM);

        // Get all the manutencaoList where km is greater than SMALLER_KM
        defaultManutencaoShouldBeFound("km.greaterThan=" + SMALLER_KM);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo equals to DEFAULT_CUSTO
        defaultManutencaoShouldBeFound("custo.equals=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo equals to UPDATED_CUSTO
        defaultManutencaoShouldNotBeFound("custo.equals=" + UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo not equals to DEFAULT_CUSTO
        defaultManutencaoShouldNotBeFound("custo.notEquals=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo not equals to UPDATED_CUSTO
        defaultManutencaoShouldBeFound("custo.notEquals=" + UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsInShouldWork() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo in DEFAULT_CUSTO or UPDATED_CUSTO
        defaultManutencaoShouldBeFound("custo.in=" + DEFAULT_CUSTO + "," + UPDATED_CUSTO);

        // Get all the manutencaoList where custo equals to UPDATED_CUSTO
        defaultManutencaoShouldNotBeFound("custo.in=" + UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsNullOrNotNull() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo is not null
        defaultManutencaoShouldBeFound("custo.specified=true");

        // Get all the manutencaoList where custo is null
        defaultManutencaoShouldNotBeFound("custo.specified=false");
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo is greater than or equal to DEFAULT_CUSTO
        defaultManutencaoShouldBeFound("custo.greaterThanOrEqual=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo is greater than or equal to UPDATED_CUSTO
        defaultManutencaoShouldNotBeFound("custo.greaterThanOrEqual=" + UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo is less than or equal to DEFAULT_CUSTO
        defaultManutencaoShouldBeFound("custo.lessThanOrEqual=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo is less than or equal to SMALLER_CUSTO
        defaultManutencaoShouldNotBeFound("custo.lessThanOrEqual=" + SMALLER_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsLessThanSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo is less than DEFAULT_CUSTO
        defaultManutencaoShouldNotBeFound("custo.lessThan=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo is less than UPDATED_CUSTO
        defaultManutencaoShouldBeFound("custo.lessThan=" + UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByCustoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        // Get all the manutencaoList where custo is greater than DEFAULT_CUSTO
        defaultManutencaoShouldNotBeFound("custo.greaterThan=" + DEFAULT_CUSTO);

        // Get all the manutencaoList where custo is greater than SMALLER_CUSTO
        defaultManutencaoShouldBeFound("custo.greaterThan=" + SMALLER_CUSTO);
    }

    @Test
    @Transactional
    void getAllManutencaosByVeiculoIsEqualToSomething() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);
        Veiculo veiculo = VeiculoResourceIT.createEntity(em);
        em.persist(veiculo);
        em.flush();
        manutencao.setVeiculo(veiculo);
        manutencaoRepository.saveAndFlush(manutencao);
        Long veiculoId = veiculo.getId();

        // Get all the manutencaoList where veiculo equals to veiculoId
        defaultManutencaoShouldBeFound("veiculoId.equals=" + veiculoId);

        // Get all the manutencaoList where veiculo equals to (veiculoId + 1)
        defaultManutencaoShouldNotBeFound("veiculoId.equals=" + (veiculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManutencaoShouldBeFound(String filter) throws Exception {
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manutencao.getId().intValue())))
            .andExpect(jsonPath("$.[*].km").value(hasItem(DEFAULT_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].custo").value(hasItem(DEFAULT_CUSTO.doubleValue())));

        // Check, that the count call also returns 1
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManutencaoShouldNotBeFound(String filter) throws Exception {
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManutencaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingManutencao() throws Exception {
        // Get the manutencao
        restManutencaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();

        // Update the manutencao
        Manutencao updatedManutencao = manutencaoRepository.findById(manutencao.getId()).get();
        // Disconnect from session so that the updates on updatedManutencao are not directly saved in db
        em.detach(updatedManutencao);
        updatedManutencao.km(UPDATED_KM).descricao(UPDATED_DESCRICAO).custo(UPDATED_CUSTO);
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(updatedManutencao);

        restManutencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manutencaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getKm()).isEqualTo(UPDATED_KM);
        assertThat(testManutencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testManutencao.getCusto()).isEqualTo(UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void putNonExistingManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manutencaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manutencaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManutencaoWithPatch() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();

        // Update the manutencao using partial update
        Manutencao partialUpdatedManutencao = new Manutencao();
        partialUpdatedManutencao.setId(manutencao.getId());

        partialUpdatedManutencao.descricao(UPDATED_DESCRICAO);

        restManutencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManutencao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManutencao))
            )
            .andExpect(status().isOk());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getKm()).isEqualTo(DEFAULT_KM);
        assertThat(testManutencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testManutencao.getCusto()).isEqualTo(DEFAULT_CUSTO);
    }

    @Test
    @Transactional
    void fullUpdateManutencaoWithPatch() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();

        // Update the manutencao using partial update
        Manutencao partialUpdatedManutencao = new Manutencao();
        partialUpdatedManutencao.setId(manutencao.getId());

        partialUpdatedManutencao.km(UPDATED_KM).descricao(UPDATED_DESCRICAO).custo(UPDATED_CUSTO);

        restManutencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManutencao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManutencao))
            )
            .andExpect(status().isOk());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
        Manutencao testManutencao = manutencaoList.get(manutencaoList.size() - 1);
        assertThat(testManutencao.getKm()).isEqualTo(UPDATED_KM);
        assertThat(testManutencao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testManutencao.getCusto()).isEqualTo(UPDATED_CUSTO);
    }

    @Test
    @Transactional
    void patchNonExistingManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manutencaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManutencao() throws Exception {
        int databaseSizeBeforeUpdate = manutencaoRepository.findAll().size();
        manutencao.setId(count.incrementAndGet());

        // Create the Manutencao
        ManutencaoDTO manutencaoDTO = manutencaoMapper.toDto(manutencao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManutencaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(manutencaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manutencao in the database
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManutencao() throws Exception {
        // Initialize the database
        manutencaoRepository.saveAndFlush(manutencao);

        int databaseSizeBeforeDelete = manutencaoRepository.findAll().size();

        // Delete the manutencao
        restManutencaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, manutencao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manutencao> manutencaoList = manutencaoRepository.findAll();
        assertThat(manutencaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
