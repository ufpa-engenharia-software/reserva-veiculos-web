package br.ufpa.facomp.veiculos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao;
import br.ufpa.facomp.veiculos.domain.Solicitacao;
import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import br.ufpa.facomp.veiculos.repository.AvaliacaoSolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.AvaliacaoSolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.AvaliacaoSolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.AvaliacaoSolicitacaoMapper;
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
 * Integration tests for the {@link AvaliacaoSolicitacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvaliacaoSolicitacaoResourceIT {

    private static final Double DEFAULT_VALOR_GASOLINA = 1D;
    private static final Double UPDATED_VALOR_GASOLINA = 2D;
    private static final Double SMALLER_VALOR_GASOLINA = 1D - 1D;

    private static final Double DEFAULT_TOTAL_GASTO = 1D;
    private static final Double UPDATED_TOTAL_GASTO = 2D;
    private static final Double SMALLER_TOTAL_GASTO = 1D - 1D;

    private static final Status DEFAULT_STATUS_SOLICITACAO = Status.PENDENTE;
    private static final Status UPDATED_STATUS_SOLICITACAO = Status.DEFERIDO;

    private static final String DEFAULT_JUSTIFICATIVA_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATIVA_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/avaliacao-solicitacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvaliacaoSolicitacaoRepository avaliacaoSolicitacaoRepository;

    @Autowired
    private AvaliacaoSolicitacaoMapper avaliacaoSolicitacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvaliacaoSolicitacaoMockMvc;

    private AvaliacaoSolicitacao avaliacaoSolicitacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvaliacaoSolicitacao createEntity(EntityManager em) {
        AvaliacaoSolicitacao avaliacaoSolicitacao = new AvaliacaoSolicitacao()
            .valorGasolina(DEFAULT_VALOR_GASOLINA)
            .totalGasto(DEFAULT_TOTAL_GASTO)
            .statusSolicitacao(DEFAULT_STATUS_SOLICITACAO)
            .justificativaStatus(DEFAULT_JUSTIFICATIVA_STATUS);
        return avaliacaoSolicitacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvaliacaoSolicitacao createUpdatedEntity(EntityManager em) {
        AvaliacaoSolicitacao avaliacaoSolicitacao = new AvaliacaoSolicitacao()
            .valorGasolina(UPDATED_VALOR_GASOLINA)
            .totalGasto(UPDATED_TOTAL_GASTO)
            .statusSolicitacao(UPDATED_STATUS_SOLICITACAO)
            .justificativaStatus(UPDATED_JUSTIFICATIVA_STATUS);
        return avaliacaoSolicitacao;
    }

    @BeforeEach
    public void initTest() {
        avaliacaoSolicitacao = createEntity(em);
    }

    @Test
    @Transactional
    void createAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeCreate = avaliacaoSolicitacaoRepository.findAll().size();
        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeCreate + 1);
        AvaliacaoSolicitacao testAvaliacaoSolicitacao = avaliacaoSolicitacaoList.get(avaliacaoSolicitacaoList.size() - 1);
        assertThat(testAvaliacaoSolicitacao.getValorGasolina()).isEqualTo(DEFAULT_VALOR_GASOLINA);
        assertThat(testAvaliacaoSolicitacao.getTotalGasto()).isEqualTo(DEFAULT_TOTAL_GASTO);
        assertThat(testAvaliacaoSolicitacao.getStatusSolicitacao()).isEqualTo(DEFAULT_STATUS_SOLICITACAO);
        assertThat(testAvaliacaoSolicitacao.getJustificativaStatus()).isEqualTo(DEFAULT_JUSTIFICATIVA_STATUS);
    }

    @Test
    @Transactional
    void createAvaliacaoSolicitacaoWithExistingId() throws Exception {
        // Create the AvaliacaoSolicitacao with an existing ID
        avaliacaoSolicitacao.setId(1L);
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        int databaseSizeBeforeCreate = avaliacaoSolicitacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaos() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avaliacaoSolicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorGasolina").value(hasItem(DEFAULT_VALOR_GASOLINA.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGasto").value(hasItem(DEFAULT_TOTAL_GASTO.doubleValue())))
            .andExpect(jsonPath("$.[*].statusSolicitacao").value(hasItem(DEFAULT_STATUS_SOLICITACAO.toString())))
            .andExpect(jsonPath("$.[*].justificativaStatus").value(hasItem(DEFAULT_JUSTIFICATIVA_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAvaliacaoSolicitacao() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get the avaliacaoSolicitacao
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, avaliacaoSolicitacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avaliacaoSolicitacao.getId().intValue()))
            .andExpect(jsonPath("$.valorGasolina").value(DEFAULT_VALOR_GASOLINA.doubleValue()))
            .andExpect(jsonPath("$.totalGasto").value(DEFAULT_TOTAL_GASTO.doubleValue()))
            .andExpect(jsonPath("$.statusSolicitacao").value(DEFAULT_STATUS_SOLICITACAO.toString()))
            .andExpect(jsonPath("$.justificativaStatus").value(DEFAULT_JUSTIFICATIVA_STATUS.toString()));
    }

    @Test
    @Transactional
    void getAvaliacaoSolicitacaosByIdFiltering() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        Long id = avaliacaoSolicitacao.getId();

        defaultAvaliacaoSolicitacaoShouldBeFound("id.equals=" + id);
        defaultAvaliacaoSolicitacaoShouldNotBeFound("id.notEquals=" + id);

        defaultAvaliacaoSolicitacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAvaliacaoSolicitacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultAvaliacaoSolicitacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAvaliacaoSolicitacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina equals to DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.equals=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina equals to UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.equals=" + UPDATED_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina not equals to DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.notEquals=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina not equals to UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.notEquals=" + UPDATED_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsInShouldWork() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina in DEFAULT_VALOR_GASOLINA or UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.in=" + DEFAULT_VALOR_GASOLINA + "," + UPDATED_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina equals to UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.in=" + UPDATED_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsNullOrNotNull() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is not null
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.specified=true");

        // Get all the avaliacaoSolicitacaoList where valorGasolina is null
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.specified=false");
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is greater than or equal to DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.greaterThanOrEqual=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is greater than or equal to UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.greaterThanOrEqual=" + UPDATED_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is less than or equal to DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.lessThanOrEqual=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is less than or equal to SMALLER_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.lessThanOrEqual=" + SMALLER_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsLessThanSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is less than DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.lessThan=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is less than UPDATED_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.lessThan=" + UPDATED_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByValorGasolinaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is greater than DEFAULT_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldNotBeFound("valorGasolina.greaterThan=" + DEFAULT_VALOR_GASOLINA);

        // Get all the avaliacaoSolicitacaoList where valorGasolina is greater than SMALLER_VALOR_GASOLINA
        defaultAvaliacaoSolicitacaoShouldBeFound("valorGasolina.greaterThan=" + SMALLER_VALOR_GASOLINA);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto equals to DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.equals=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto equals to UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.equals=" + UPDATED_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto not equals to DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.notEquals=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto not equals to UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.notEquals=" + UPDATED_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsInShouldWork() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto in DEFAULT_TOTAL_GASTO or UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.in=" + DEFAULT_TOTAL_GASTO + "," + UPDATED_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto equals to UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.in=" + UPDATED_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsNullOrNotNull() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto is not null
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.specified=true");

        // Get all the avaliacaoSolicitacaoList where totalGasto is null
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.specified=false");
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto is greater than or equal to DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.greaterThanOrEqual=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto is greater than or equal to UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.greaterThanOrEqual=" + UPDATED_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto is less than or equal to DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.lessThanOrEqual=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto is less than or equal to SMALLER_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.lessThanOrEqual=" + SMALLER_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsLessThanSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto is less than DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.lessThan=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto is less than UPDATED_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.lessThan=" + UPDATED_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByTotalGastoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where totalGasto is greater than DEFAULT_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("totalGasto.greaterThan=" + DEFAULT_TOTAL_GASTO);

        // Get all the avaliacaoSolicitacaoList where totalGasto is greater than SMALLER_TOTAL_GASTO
        defaultAvaliacaoSolicitacaoShouldBeFound("totalGasto.greaterThan=" + SMALLER_TOTAL_GASTO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByStatusSolicitacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao equals to DEFAULT_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldBeFound("statusSolicitacao.equals=" + DEFAULT_STATUS_SOLICITACAO);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao equals to UPDATED_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("statusSolicitacao.equals=" + UPDATED_STATUS_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByStatusSolicitacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao not equals to DEFAULT_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("statusSolicitacao.notEquals=" + DEFAULT_STATUS_SOLICITACAO);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao not equals to UPDATED_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldBeFound("statusSolicitacao.notEquals=" + UPDATED_STATUS_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByStatusSolicitacaoIsInShouldWork() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao in DEFAULT_STATUS_SOLICITACAO or UPDATED_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldBeFound("statusSolicitacao.in=" + DEFAULT_STATUS_SOLICITACAO + "," + UPDATED_STATUS_SOLICITACAO);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao equals to UPDATED_STATUS_SOLICITACAO
        defaultAvaliacaoSolicitacaoShouldNotBeFound("statusSolicitacao.in=" + UPDATED_STATUS_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosByStatusSolicitacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao is not null
        defaultAvaliacaoSolicitacaoShouldBeFound("statusSolicitacao.specified=true");

        // Get all the avaliacaoSolicitacaoList where statusSolicitacao is null
        defaultAvaliacaoSolicitacaoShouldNotBeFound("statusSolicitacao.specified=false");
    }

    @Test
    @Transactional
    void getAllAvaliacaoSolicitacaosBySolicitacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);
        Solicitacao solicitacao = SolicitacaoResourceIT.createEntity(em);
        em.persist(solicitacao);
        em.flush();
        avaliacaoSolicitacao.setSolicitacao(solicitacao);
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);
        Long solicitacaoId = solicitacao.getId();

        // Get all the avaliacaoSolicitacaoList where solicitacao equals to solicitacaoId
        defaultAvaliacaoSolicitacaoShouldBeFound("solicitacaoId.equals=" + solicitacaoId);

        // Get all the avaliacaoSolicitacaoList where solicitacao equals to (solicitacaoId + 1)
        defaultAvaliacaoSolicitacaoShouldNotBeFound("solicitacaoId.equals=" + (solicitacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvaliacaoSolicitacaoShouldBeFound(String filter) throws Exception {
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avaliacaoSolicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorGasolina").value(hasItem(DEFAULT_VALOR_GASOLINA.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGasto").value(hasItem(DEFAULT_TOTAL_GASTO.doubleValue())))
            .andExpect(jsonPath("$.[*].statusSolicitacao").value(hasItem(DEFAULT_STATUS_SOLICITACAO.toString())))
            .andExpect(jsonPath("$.[*].justificativaStatus").value(hasItem(DEFAULT_JUSTIFICATIVA_STATUS.toString())));

        // Check, that the count call also returns 1
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvaliacaoSolicitacaoShouldNotBeFound(String filter) throws Exception {
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvaliacaoSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvaliacaoSolicitacao() throws Exception {
        // Get the avaliacaoSolicitacao
        restAvaliacaoSolicitacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAvaliacaoSolicitacao() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();

        // Update the avaliacaoSolicitacao
        AvaliacaoSolicitacao updatedAvaliacaoSolicitacao = avaliacaoSolicitacaoRepository.findById(avaliacaoSolicitacao.getId()).get();
        // Disconnect from session so that the updates on updatedAvaliacaoSolicitacao are not directly saved in db
        em.detach(updatedAvaliacaoSolicitacao);
        updatedAvaliacaoSolicitacao
            .valorGasolina(UPDATED_VALOR_GASOLINA)
            .totalGasto(UPDATED_TOTAL_GASTO)
            .statusSolicitacao(UPDATED_STATUS_SOLICITACAO)
            .justificativaStatus(UPDATED_JUSTIFICATIVA_STATUS);
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(updatedAvaliacaoSolicitacao);

        restAvaliacaoSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avaliacaoSolicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
        AvaliacaoSolicitacao testAvaliacaoSolicitacao = avaliacaoSolicitacaoList.get(avaliacaoSolicitacaoList.size() - 1);
        assertThat(testAvaliacaoSolicitacao.getValorGasolina()).isEqualTo(UPDATED_VALOR_GASOLINA);
        assertThat(testAvaliacaoSolicitacao.getTotalGasto()).isEqualTo(UPDATED_TOTAL_GASTO);
        assertThat(testAvaliacaoSolicitacao.getStatusSolicitacao()).isEqualTo(UPDATED_STATUS_SOLICITACAO);
        assertThat(testAvaliacaoSolicitacao.getJustificativaStatus()).isEqualTo(UPDATED_JUSTIFICATIVA_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avaliacaoSolicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvaliacaoSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();

        // Update the avaliacaoSolicitacao using partial update
        AvaliacaoSolicitacao partialUpdatedAvaliacaoSolicitacao = new AvaliacaoSolicitacao();
        partialUpdatedAvaliacaoSolicitacao.setId(avaliacaoSolicitacao.getId());

        partialUpdatedAvaliacaoSolicitacao
            .valorGasolina(UPDATED_VALOR_GASOLINA)
            .totalGasto(UPDATED_TOTAL_GASTO)
            .statusSolicitacao(UPDATED_STATUS_SOLICITACAO)
            .justificativaStatus(UPDATED_JUSTIFICATIVA_STATUS);

        restAvaliacaoSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacaoSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvaliacaoSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
        AvaliacaoSolicitacao testAvaliacaoSolicitacao = avaliacaoSolicitacaoList.get(avaliacaoSolicitacaoList.size() - 1);
        assertThat(testAvaliacaoSolicitacao.getValorGasolina()).isEqualTo(UPDATED_VALOR_GASOLINA);
        assertThat(testAvaliacaoSolicitacao.getTotalGasto()).isEqualTo(UPDATED_TOTAL_GASTO);
        assertThat(testAvaliacaoSolicitacao.getStatusSolicitacao()).isEqualTo(UPDATED_STATUS_SOLICITACAO);
        assertThat(testAvaliacaoSolicitacao.getJustificativaStatus()).isEqualTo(UPDATED_JUSTIFICATIVA_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAvaliacaoSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();

        // Update the avaliacaoSolicitacao using partial update
        AvaliacaoSolicitacao partialUpdatedAvaliacaoSolicitacao = new AvaliacaoSolicitacao();
        partialUpdatedAvaliacaoSolicitacao.setId(avaliacaoSolicitacao.getId());

        partialUpdatedAvaliacaoSolicitacao
            .valorGasolina(UPDATED_VALOR_GASOLINA)
            .totalGasto(UPDATED_TOTAL_GASTO)
            .statusSolicitacao(UPDATED_STATUS_SOLICITACAO)
            .justificativaStatus(UPDATED_JUSTIFICATIVA_STATUS);

        restAvaliacaoSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacaoSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvaliacaoSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
        AvaliacaoSolicitacao testAvaliacaoSolicitacao = avaliacaoSolicitacaoList.get(avaliacaoSolicitacaoList.size() - 1);
        assertThat(testAvaliacaoSolicitacao.getValorGasolina()).isEqualTo(UPDATED_VALOR_GASOLINA);
        assertThat(testAvaliacaoSolicitacao.getTotalGasto()).isEqualTo(UPDATED_TOTAL_GASTO);
        assertThat(testAvaliacaoSolicitacao.getStatusSolicitacao()).isEqualTo(UPDATED_STATUS_SOLICITACAO);
        assertThat(testAvaliacaoSolicitacao.getJustificativaStatus()).isEqualTo(UPDATED_JUSTIFICATIVA_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avaliacaoSolicitacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvaliacaoSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoSolicitacaoRepository.findAll().size();
        avaliacaoSolicitacao.setId(count.incrementAndGet());

        // Create the AvaliacaoSolicitacao
        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = avaliacaoSolicitacaoMapper.toDto(avaliacaoSolicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avaliacaoSolicitacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvaliacaoSolicitacao in the database
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvaliacaoSolicitacao() throws Exception {
        // Initialize the database
        avaliacaoSolicitacaoRepository.saveAndFlush(avaliacaoSolicitacao);

        int databaseSizeBeforeDelete = avaliacaoSolicitacaoRepository.findAll().size();

        // Delete the avaliacaoSolicitacao
        restAvaliacaoSolicitacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, avaliacaoSolicitacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AvaliacaoSolicitacao> avaliacaoSolicitacaoList = avaliacaoSolicitacaoRepository.findAll();
        assertThat(avaliacaoSolicitacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
