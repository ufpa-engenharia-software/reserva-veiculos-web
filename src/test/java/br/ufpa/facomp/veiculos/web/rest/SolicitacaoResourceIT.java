package br.ufpa.facomp.veiculos.web.rest;

import static br.ufpa.facomp.veiculos.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao;
import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import br.ufpa.facomp.veiculos.domain.Solicitacao;
import br.ufpa.facomp.veiculos.domain.Usuario;
import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import br.ufpa.facomp.veiculos.repository.SolicitacaoRepository;
import br.ufpa.facomp.veiculos.service.criteria.SolicitacaoCriteria;
import br.ufpa.facomp.veiculos.service.dto.SolicitacaoDTO;
import br.ufpa.facomp.veiculos.service.mapper.SolicitacaoMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SolicitacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SolicitacaoResourceIT {

    private static final String DEFAULT_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_ORIGEM = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINO = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_SOLICITACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_SOLICITACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_SOLICITACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_HORARIO_SAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORARIO_SAIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HORARIO_SAIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_HORARIO_RETORNO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORARIO_RETORNO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HORARIO_RETORNO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_DISTANCIA_ESTIMADA_KM = 1D;
    private static final Double UPDATED_DISTANCIA_ESTIMADA_KM = 2D;
    private static final Double SMALLER_DISTANCIA_ESTIMADA_KM = 1D - 1D;

    private static final String DEFAULT_JUSTIFICATIVA = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATIVA = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDENTE;
    private static final Status UPDATED_STATUS = Status.DEFERIDO;

    private static final Integer DEFAULT_N_PESSOAS = 1;
    private static final Integer UPDATED_N_PESSOAS = 2;
    private static final Integer SMALLER_N_PESSOAS = 1 - 1;

    private static final Double DEFAULT_PESO = 1D;
    private static final Double UPDATED_PESO = 2D;
    private static final Double SMALLER_PESO = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/solicitacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private SolicitacaoMapper solicitacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolicitacaoMockMvc;

    private Solicitacao solicitacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitacao createEntity(EntityManager em) {
        Solicitacao solicitacao = new Solicitacao()
            .origem(DEFAULT_ORIGEM)
            .destino(DEFAULT_DESTINO)
            .dataSolicitacao(DEFAULT_DATA_SOLICITACAO)
            .horarioSaida(DEFAULT_HORARIO_SAIDA)
            .horarioRetorno(DEFAULT_HORARIO_RETORNO)
            .distanciaEstimadaKm(DEFAULT_DISTANCIA_ESTIMADA_KM)
            .justificativa(DEFAULT_JUSTIFICATIVA)
            .status(DEFAULT_STATUS)
            .nPessoas(DEFAULT_N_PESSOAS)
            .peso(DEFAULT_PESO);
        return solicitacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitacao createUpdatedEntity(EntityManager em) {
        Solicitacao solicitacao = new Solicitacao()
            .origem(UPDATED_ORIGEM)
            .destino(UPDATED_DESTINO)
            .dataSolicitacao(UPDATED_DATA_SOLICITACAO)
            .horarioSaida(UPDATED_HORARIO_SAIDA)
            .horarioRetorno(UPDATED_HORARIO_RETORNO)
            .distanciaEstimadaKm(UPDATED_DISTANCIA_ESTIMADA_KM)
            .justificativa(UPDATED_JUSTIFICATIVA)
            .status(UPDATED_STATUS)
            .nPessoas(UPDATED_N_PESSOAS)
            .peso(UPDATED_PESO);
        return solicitacao;
    }

    @BeforeEach
    public void initTest() {
        solicitacao = createEntity(em);
    }

    @Test
    @Transactional
    void createSolicitacao() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoRepository.findAll().size();
        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);
        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getOrigem()).isEqualTo(DEFAULT_ORIGEM);
        assertThat(testSolicitacao.getDestino()).isEqualTo(DEFAULT_DESTINO);
        assertThat(testSolicitacao.getDataSolicitacao()).isEqualTo(DEFAULT_DATA_SOLICITACAO);
        assertThat(testSolicitacao.getHorarioSaida()).isEqualTo(DEFAULT_HORARIO_SAIDA);
        assertThat(testSolicitacao.getHorarioRetorno()).isEqualTo(DEFAULT_HORARIO_RETORNO);
        assertThat(testSolicitacao.getDistanciaEstimadaKm()).isEqualTo(DEFAULT_DISTANCIA_ESTIMADA_KM);
        assertThat(testSolicitacao.getJustificativa()).isEqualTo(DEFAULT_JUSTIFICATIVA);
        assertThat(testSolicitacao.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSolicitacao.getnPessoas()).isEqualTo(DEFAULT_N_PESSOAS);
        assertThat(testSolicitacao.getPeso()).isEqualTo(DEFAULT_PESO);
    }

    @Test
    @Transactional
    void createSolicitacaoWithExistingId() throws Exception {
        // Create the Solicitacao with an existing ID
        solicitacao.setId(1L);
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        int databaseSizeBeforeCreate = solicitacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSolicitacaos() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].origem").value(hasItem(DEFAULT_ORIGEM)))
            .andExpect(jsonPath("$.[*].destino").value(hasItem(DEFAULT_DESTINO)))
            .andExpect(jsonPath("$.[*].dataSolicitacao").value(hasItem(sameInstant(DEFAULT_DATA_SOLICITACAO))))
            .andExpect(jsonPath("$.[*].horarioSaida").value(hasItem(sameInstant(DEFAULT_HORARIO_SAIDA))))
            .andExpect(jsonPath("$.[*].horarioRetorno").value(hasItem(sameInstant(DEFAULT_HORARIO_RETORNO))))
            .andExpect(jsonPath("$.[*].distanciaEstimadaKm").value(hasItem(DEFAULT_DISTANCIA_ESTIMADA_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].justificativa").value(hasItem(DEFAULT_JUSTIFICATIVA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nPessoas").value(hasItem(DEFAULT_N_PESSOAS)))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())));
    }

    @Test
    @Transactional
    void getSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get the solicitacao
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, solicitacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solicitacao.getId().intValue()))
            .andExpect(jsonPath("$.origem").value(DEFAULT_ORIGEM))
            .andExpect(jsonPath("$.destino").value(DEFAULT_DESTINO))
            .andExpect(jsonPath("$.dataSolicitacao").value(sameInstant(DEFAULT_DATA_SOLICITACAO)))
            .andExpect(jsonPath("$.horarioSaida").value(sameInstant(DEFAULT_HORARIO_SAIDA)))
            .andExpect(jsonPath("$.horarioRetorno").value(sameInstant(DEFAULT_HORARIO_RETORNO)))
            .andExpect(jsonPath("$.distanciaEstimadaKm").value(DEFAULT_DISTANCIA_ESTIMADA_KM.doubleValue()))
            .andExpect(jsonPath("$.justificativa").value(DEFAULT_JUSTIFICATIVA.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.nPessoas").value(DEFAULT_N_PESSOAS))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.doubleValue()));
    }

    @Test
    @Transactional
    void getSolicitacaosByIdFiltering() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        Long id = solicitacao.getId();

        defaultSolicitacaoShouldBeFound("id.equals=" + id);
        defaultSolicitacaoShouldNotBeFound("id.notEquals=" + id);

        defaultSolicitacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSolicitacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultSolicitacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSolicitacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem equals to DEFAULT_ORIGEM
        defaultSolicitacaoShouldBeFound("origem.equals=" + DEFAULT_ORIGEM);

        // Get all the solicitacaoList where origem equals to UPDATED_ORIGEM
        defaultSolicitacaoShouldNotBeFound("origem.equals=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem not equals to DEFAULT_ORIGEM
        defaultSolicitacaoShouldNotBeFound("origem.notEquals=" + DEFAULT_ORIGEM);

        // Get all the solicitacaoList where origem not equals to UPDATED_ORIGEM
        defaultSolicitacaoShouldBeFound("origem.notEquals=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem in DEFAULT_ORIGEM or UPDATED_ORIGEM
        defaultSolicitacaoShouldBeFound("origem.in=" + DEFAULT_ORIGEM + "," + UPDATED_ORIGEM);

        // Get all the solicitacaoList where origem equals to UPDATED_ORIGEM
        defaultSolicitacaoShouldNotBeFound("origem.in=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem is not null
        defaultSolicitacaoShouldBeFound("origem.specified=true");

        // Get all the solicitacaoList where origem is null
        defaultSolicitacaoShouldNotBeFound("origem.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem contains DEFAULT_ORIGEM
        defaultSolicitacaoShouldBeFound("origem.contains=" + DEFAULT_ORIGEM);

        // Get all the solicitacaoList where origem contains UPDATED_ORIGEM
        defaultSolicitacaoShouldNotBeFound("origem.contains=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where origem does not contain DEFAULT_ORIGEM
        defaultSolicitacaoShouldNotBeFound("origem.doesNotContain=" + DEFAULT_ORIGEM);

        // Get all the solicitacaoList where origem does not contain UPDATED_ORIGEM
        defaultSolicitacaoShouldBeFound("origem.doesNotContain=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino equals to DEFAULT_DESTINO
        defaultSolicitacaoShouldBeFound("destino.equals=" + DEFAULT_DESTINO);

        // Get all the solicitacaoList where destino equals to UPDATED_DESTINO
        defaultSolicitacaoShouldNotBeFound("destino.equals=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino not equals to DEFAULT_DESTINO
        defaultSolicitacaoShouldNotBeFound("destino.notEquals=" + DEFAULT_DESTINO);

        // Get all the solicitacaoList where destino not equals to UPDATED_DESTINO
        defaultSolicitacaoShouldBeFound("destino.notEquals=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino in DEFAULT_DESTINO or UPDATED_DESTINO
        defaultSolicitacaoShouldBeFound("destino.in=" + DEFAULT_DESTINO + "," + UPDATED_DESTINO);

        // Get all the solicitacaoList where destino equals to UPDATED_DESTINO
        defaultSolicitacaoShouldNotBeFound("destino.in=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino is not null
        defaultSolicitacaoShouldBeFound("destino.specified=true");

        // Get all the solicitacaoList where destino is null
        defaultSolicitacaoShouldNotBeFound("destino.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino contains DEFAULT_DESTINO
        defaultSolicitacaoShouldBeFound("destino.contains=" + DEFAULT_DESTINO);

        // Get all the solicitacaoList where destino contains UPDATED_DESTINO
        defaultSolicitacaoShouldNotBeFound("destino.contains=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDestinoNotContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where destino does not contain DEFAULT_DESTINO
        defaultSolicitacaoShouldNotBeFound("destino.doesNotContain=" + DEFAULT_DESTINO);

        // Get all the solicitacaoList where destino does not contain UPDATED_DESTINO
        defaultSolicitacaoShouldBeFound("destino.doesNotContain=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao equals to DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.equals=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao equals to UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.equals=" + UPDATED_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao not equals to DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.notEquals=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao not equals to UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.notEquals=" + UPDATED_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao in DEFAULT_DATA_SOLICITACAO or UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.in=" + DEFAULT_DATA_SOLICITACAO + "," + UPDATED_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao equals to UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.in=" + UPDATED_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao is not null
        defaultSolicitacaoShouldBeFound("dataSolicitacao.specified=true");

        // Get all the solicitacaoList where dataSolicitacao is null
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao is greater than or equal to DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.greaterThanOrEqual=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao is greater than or equal to UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.greaterThanOrEqual=" + UPDATED_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao is less than or equal to DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.lessThanOrEqual=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao is less than or equal to SMALLER_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.lessThanOrEqual=" + SMALLER_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao is less than DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.lessThan=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao is less than UPDATED_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.lessThan=" + UPDATED_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataSolicitacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataSolicitacao is greater than DEFAULT_DATA_SOLICITACAO
        defaultSolicitacaoShouldNotBeFound("dataSolicitacao.greaterThan=" + DEFAULT_DATA_SOLICITACAO);

        // Get all the solicitacaoList where dataSolicitacao is greater than SMALLER_DATA_SOLICITACAO
        defaultSolicitacaoShouldBeFound("dataSolicitacao.greaterThan=" + SMALLER_DATA_SOLICITACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida equals to DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.equals=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida equals to UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.equals=" + UPDATED_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida not equals to DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.notEquals=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida not equals to UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.notEquals=" + UPDATED_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida in DEFAULT_HORARIO_SAIDA or UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.in=" + DEFAULT_HORARIO_SAIDA + "," + UPDATED_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida equals to UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.in=" + UPDATED_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida is not null
        defaultSolicitacaoShouldBeFound("horarioSaida.specified=true");

        // Get all the solicitacaoList where horarioSaida is null
        defaultSolicitacaoShouldNotBeFound("horarioSaida.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida is greater than or equal to DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.greaterThanOrEqual=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida is greater than or equal to UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.greaterThanOrEqual=" + UPDATED_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida is less than or equal to DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.lessThanOrEqual=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida is less than or equal to SMALLER_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.lessThanOrEqual=" + SMALLER_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida is less than DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.lessThan=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida is less than UPDATED_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.lessThan=" + UPDATED_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioSaidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioSaida is greater than DEFAULT_HORARIO_SAIDA
        defaultSolicitacaoShouldNotBeFound("horarioSaida.greaterThan=" + DEFAULT_HORARIO_SAIDA);

        // Get all the solicitacaoList where horarioSaida is greater than SMALLER_HORARIO_SAIDA
        defaultSolicitacaoShouldBeFound("horarioSaida.greaterThan=" + SMALLER_HORARIO_SAIDA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno equals to DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.equals=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno equals to UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.equals=" + UPDATED_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno not equals to DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.notEquals=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno not equals to UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.notEquals=" + UPDATED_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno in DEFAULT_HORARIO_RETORNO or UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.in=" + DEFAULT_HORARIO_RETORNO + "," + UPDATED_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno equals to UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.in=" + UPDATED_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno is not null
        defaultSolicitacaoShouldBeFound("horarioRetorno.specified=true");

        // Get all the solicitacaoList where horarioRetorno is null
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno is greater than or equal to DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.greaterThanOrEqual=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno is greater than or equal to UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.greaterThanOrEqual=" + UPDATED_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno is less than or equal to DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.lessThanOrEqual=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno is less than or equal to SMALLER_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.lessThanOrEqual=" + SMALLER_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno is less than DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.lessThan=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno is less than UPDATED_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.lessThan=" + UPDATED_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByHorarioRetornoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where horarioRetorno is greater than DEFAULT_HORARIO_RETORNO
        defaultSolicitacaoShouldNotBeFound("horarioRetorno.greaterThan=" + DEFAULT_HORARIO_RETORNO);

        // Get all the solicitacaoList where horarioRetorno is greater than SMALLER_HORARIO_RETORNO
        defaultSolicitacaoShouldBeFound("horarioRetorno.greaterThan=" + SMALLER_HORARIO_RETORNO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm equals to DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.equals=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm equals to UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.equals=" + UPDATED_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm not equals to DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.notEquals=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm not equals to UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.notEquals=" + UPDATED_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm in DEFAULT_DISTANCIA_ESTIMADA_KM or UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.in=" + DEFAULT_DISTANCIA_ESTIMADA_KM + "," + UPDATED_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm equals to UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.in=" + UPDATED_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm is not null
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.specified=true");

        // Get all the solicitacaoList where distanciaEstimadaKm is null
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm is greater than or equal to DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.greaterThanOrEqual=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm is greater than or equal to UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.greaterThanOrEqual=" + UPDATED_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm is less than or equal to DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.lessThanOrEqual=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm is less than or equal to SMALLER_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.lessThanOrEqual=" + SMALLER_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm is less than DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.lessThan=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm is less than UPDATED_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.lessThan=" + UPDATED_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDistanciaEstimadaKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where distanciaEstimadaKm is greater than DEFAULT_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldNotBeFound("distanciaEstimadaKm.greaterThan=" + DEFAULT_DISTANCIA_ESTIMADA_KM);

        // Get all the solicitacaoList where distanciaEstimadaKm is greater than SMALLER_DISTANCIA_ESTIMADA_KM
        defaultSolicitacaoShouldBeFound("distanciaEstimadaKm.greaterThan=" + SMALLER_DISTANCIA_ESTIMADA_KM);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status equals to DEFAULT_STATUS
        defaultSolicitacaoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the solicitacaoList where status equals to UPDATED_STATUS
        defaultSolicitacaoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status not equals to DEFAULT_STATUS
        defaultSolicitacaoShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the solicitacaoList where status not equals to UPDATED_STATUS
        defaultSolicitacaoShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSolicitacaoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the solicitacaoList where status equals to UPDATED_STATUS
        defaultSolicitacaoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status is not null
        defaultSolicitacaoShouldBeFound("status.specified=true");

        // Get all the solicitacaoList where status is null
        defaultSolicitacaoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas equals to DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.equals=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas equals to UPDATED_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.equals=" + UPDATED_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas not equals to DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.notEquals=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas not equals to UPDATED_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.notEquals=" + UPDATED_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas in DEFAULT_N_PESSOAS or UPDATED_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.in=" + DEFAULT_N_PESSOAS + "," + UPDATED_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas equals to UPDATED_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.in=" + UPDATED_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas is not null
        defaultSolicitacaoShouldBeFound("nPessoas.specified=true");

        // Get all the solicitacaoList where nPessoas is null
        defaultSolicitacaoShouldNotBeFound("nPessoas.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas is greater than or equal to DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.greaterThanOrEqual=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas is greater than or equal to UPDATED_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.greaterThanOrEqual=" + UPDATED_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas is less than or equal to DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.lessThanOrEqual=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas is less than or equal to SMALLER_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.lessThanOrEqual=" + SMALLER_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas is less than DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.lessThan=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas is less than UPDATED_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.lessThan=" + UPDATED_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosBynPessoasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where nPessoas is greater than DEFAULT_N_PESSOAS
        defaultSolicitacaoShouldNotBeFound("nPessoas.greaterThan=" + DEFAULT_N_PESSOAS);

        // Get all the solicitacaoList where nPessoas is greater than SMALLER_N_PESSOAS
        defaultSolicitacaoShouldBeFound("nPessoas.greaterThan=" + SMALLER_N_PESSOAS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso equals to DEFAULT_PESO
        defaultSolicitacaoShouldBeFound("peso.equals=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso equals to UPDATED_PESO
        defaultSolicitacaoShouldNotBeFound("peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso not equals to DEFAULT_PESO
        defaultSolicitacaoShouldNotBeFound("peso.notEquals=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso not equals to UPDATED_PESO
        defaultSolicitacaoShouldBeFound("peso.notEquals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso in DEFAULT_PESO or UPDATED_PESO
        defaultSolicitacaoShouldBeFound("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO);

        // Get all the solicitacaoList where peso equals to UPDATED_PESO
        defaultSolicitacaoShouldNotBeFound("peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso is not null
        defaultSolicitacaoShouldBeFound("peso.specified=true");

        // Get all the solicitacaoList where peso is null
        defaultSolicitacaoShouldNotBeFound("peso.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso is greater than or equal to DEFAULT_PESO
        defaultSolicitacaoShouldBeFound("peso.greaterThanOrEqual=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso is greater than or equal to UPDATED_PESO
        defaultSolicitacaoShouldNotBeFound("peso.greaterThanOrEqual=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso is less than or equal to DEFAULT_PESO
        defaultSolicitacaoShouldBeFound("peso.lessThanOrEqual=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso is less than or equal to SMALLER_PESO
        defaultSolicitacaoShouldNotBeFound("peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso is less than DEFAULT_PESO
        defaultSolicitacaoShouldNotBeFound("peso.lessThan=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso is less than UPDATED_PESO
        defaultSolicitacaoShouldBeFound("peso.lessThan=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where peso is greater than DEFAULT_PESO
        defaultSolicitacaoShouldNotBeFound("peso.greaterThan=" + DEFAULT_PESO);

        // Get all the solicitacaoList where peso is greater than SMALLER_PESO
        defaultSolicitacaoShouldBeFound("peso.greaterThan=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        CategoriaVeiculo categoria = CategoriaVeiculoResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        solicitacao.setCategoria(categoria);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long categoriaId = categoria.getId();

        // Get all the solicitacaoList where categoria equals to categoriaId
        defaultSolicitacaoShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the solicitacaoList where categoria equals to (categoriaId + 1)
        defaultSolicitacaoShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosByVeiculoAlocadoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        Veiculo veiculoAlocado = VeiculoResourceIT.createEntity(em);
        em.persist(veiculoAlocado);
        em.flush();
        solicitacao.setVeiculoAlocado(veiculoAlocado);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long veiculoAlocadoId = veiculoAlocado.getId();

        // Get all the solicitacaoList where veiculoAlocado equals to veiculoAlocadoId
        defaultSolicitacaoShouldBeFound("veiculoAlocadoId.equals=" + veiculoAlocadoId);

        // Get all the solicitacaoList where veiculoAlocado equals to (veiculoAlocadoId + 1)
        defaultSolicitacaoShouldNotBeFound("veiculoAlocadoId.equals=" + (veiculoAlocadoId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosByAvaliacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        AvaliacaoSolicitacao avaliacao = AvaliacaoSolicitacaoResourceIT.createEntity(em);
        em.persist(avaliacao);
        em.flush();
        solicitacao.setAvaliacao(avaliacao);
        avaliacao.setSolicitacao(solicitacao);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long avaliacaoId = avaliacao.getId();

        // Get all the solicitacaoList where avaliacao equals to avaliacaoId
        defaultSolicitacaoShouldBeFound("avaliacaoId.equals=" + avaliacaoId);

        // Get all the solicitacaoList where avaliacao equals to (avaliacaoId + 1)
        defaultSolicitacaoShouldNotBeFound("avaliacaoId.equals=" + (avaliacaoId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosBySolicitanteIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        Usuario solicitante = UsuarioResourceIT.createEntity(em);
        em.persist(solicitante);
        em.flush();
        solicitacao.setSolicitante(solicitante);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long solicitanteId = solicitante.getId();

        // Get all the solicitacaoList where solicitante equals to solicitanteId
        defaultSolicitacaoShouldBeFound("solicitanteId.equals=" + solicitanteId);

        // Get all the solicitacaoList where solicitante equals to (solicitanteId + 1)
        defaultSolicitacaoShouldNotBeFound("solicitanteId.equals=" + (solicitanteId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosByAutorizadorIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        Usuario autorizador = UsuarioResourceIT.createEntity(em);
        em.persist(autorizador);
        em.flush();
        solicitacao.setAutorizador(autorizador);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long autorizadorId = autorizador.getId();

        // Get all the solicitacaoList where autorizador equals to autorizadorId
        defaultSolicitacaoShouldBeFound("autorizadorId.equals=" + autorizadorId);

        // Get all the solicitacaoList where autorizador equals to (autorizadorId + 1)
        defaultSolicitacaoShouldNotBeFound("autorizadorId.equals=" + (autorizadorId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosByMotoristaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);
        Usuario motorista = UsuarioResourceIT.createEntity(em);
        em.persist(motorista);
        em.flush();
        solicitacao.setMotorista(motorista);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long motoristaId = motorista.getId();

        // Get all the solicitacaoList where motorista equals to motoristaId
        defaultSolicitacaoShouldBeFound("motoristaId.equals=" + motoristaId);

        // Get all the solicitacaoList where motorista equals to (motoristaId + 1)
        defaultSolicitacaoShouldNotBeFound("motoristaId.equals=" + (motoristaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSolicitacaoShouldBeFound(String filter) throws Exception {
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].origem").value(hasItem(DEFAULT_ORIGEM)))
            .andExpect(jsonPath("$.[*].destino").value(hasItem(DEFAULT_DESTINO)))
            .andExpect(jsonPath("$.[*].dataSolicitacao").value(hasItem(sameInstant(DEFAULT_DATA_SOLICITACAO))))
            .andExpect(jsonPath("$.[*].horarioSaida").value(hasItem(sameInstant(DEFAULT_HORARIO_SAIDA))))
            .andExpect(jsonPath("$.[*].horarioRetorno").value(hasItem(sameInstant(DEFAULT_HORARIO_RETORNO))))
            .andExpect(jsonPath("$.[*].distanciaEstimadaKm").value(hasItem(DEFAULT_DISTANCIA_ESTIMADA_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].justificativa").value(hasItem(DEFAULT_JUSTIFICATIVA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nPessoas").value(hasItem(DEFAULT_N_PESSOAS)))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.doubleValue())));

        // Check, that the count call also returns 1
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSolicitacaoShouldNotBeFound(String filter) throws Exception {
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSolicitacao() throws Exception {
        // Get the solicitacao
        restSolicitacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao
        Solicitacao updatedSolicitacao = solicitacaoRepository.findById(solicitacao.getId()).get();
        // Disconnect from session so that the updates on updatedSolicitacao are not directly saved in db
        em.detach(updatedSolicitacao);
        updatedSolicitacao
            .origem(UPDATED_ORIGEM)
            .destino(UPDATED_DESTINO)
            .dataSolicitacao(UPDATED_DATA_SOLICITACAO)
            .horarioSaida(UPDATED_HORARIO_SAIDA)
            .horarioRetorno(UPDATED_HORARIO_RETORNO)
            .distanciaEstimadaKm(UPDATED_DISTANCIA_ESTIMADA_KM)
            .justificativa(UPDATED_JUSTIFICATIVA)
            .status(UPDATED_STATUS)
            .nPessoas(UPDATED_N_PESSOAS)
            .peso(UPDATED_PESO);
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(updatedSolicitacao);

        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getOrigem()).isEqualTo(UPDATED_ORIGEM);
        assertThat(testSolicitacao.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testSolicitacao.getDataSolicitacao()).isEqualTo(UPDATED_DATA_SOLICITACAO);
        assertThat(testSolicitacao.getHorarioSaida()).isEqualTo(UPDATED_HORARIO_SAIDA);
        assertThat(testSolicitacao.getHorarioRetorno()).isEqualTo(UPDATED_HORARIO_RETORNO);
        assertThat(testSolicitacao.getDistanciaEstimadaKm()).isEqualTo(UPDATED_DISTANCIA_ESTIMADA_KM);
        assertThat(testSolicitacao.getJustificativa()).isEqualTo(UPDATED_JUSTIFICATIVA);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getnPessoas()).isEqualTo(UPDATED_N_PESSOAS);
        assertThat(testSolicitacao.getPeso()).isEqualTo(UPDATED_PESO);
    }

    @Test
    @Transactional
    void putNonExistingSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao using partial update
        Solicitacao partialUpdatedSolicitacao = new Solicitacao();
        partialUpdatedSolicitacao.setId(solicitacao.getId());

        partialUpdatedSolicitacao
            .destino(UPDATED_DESTINO)
            .dataSolicitacao(UPDATED_DATA_SOLICITACAO)
            .horarioRetorno(UPDATED_HORARIO_RETORNO)
            .distanciaEstimadaKm(UPDATED_DISTANCIA_ESTIMADA_KM)
            .status(UPDATED_STATUS)
            .nPessoas(UPDATED_N_PESSOAS)
            .peso(UPDATED_PESO);

        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getOrigem()).isEqualTo(DEFAULT_ORIGEM);
        assertThat(testSolicitacao.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testSolicitacao.getDataSolicitacao()).isEqualTo(UPDATED_DATA_SOLICITACAO);
        assertThat(testSolicitacao.getHorarioSaida()).isEqualTo(DEFAULT_HORARIO_SAIDA);
        assertThat(testSolicitacao.getHorarioRetorno()).isEqualTo(UPDATED_HORARIO_RETORNO);
        assertThat(testSolicitacao.getDistanciaEstimadaKm()).isEqualTo(UPDATED_DISTANCIA_ESTIMADA_KM);
        assertThat(testSolicitacao.getJustificativa()).isEqualTo(DEFAULT_JUSTIFICATIVA);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getnPessoas()).isEqualTo(UPDATED_N_PESSOAS);
        assertThat(testSolicitacao.getPeso()).isEqualTo(UPDATED_PESO);
    }

    @Test
    @Transactional
    void fullUpdateSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao using partial update
        Solicitacao partialUpdatedSolicitacao = new Solicitacao();
        partialUpdatedSolicitacao.setId(solicitacao.getId());

        partialUpdatedSolicitacao
            .origem(UPDATED_ORIGEM)
            .destino(UPDATED_DESTINO)
            .dataSolicitacao(UPDATED_DATA_SOLICITACAO)
            .horarioSaida(UPDATED_HORARIO_SAIDA)
            .horarioRetorno(UPDATED_HORARIO_RETORNO)
            .distanciaEstimadaKm(UPDATED_DISTANCIA_ESTIMADA_KM)
            .justificativa(UPDATED_JUSTIFICATIVA)
            .status(UPDATED_STATUS)
            .nPessoas(UPDATED_N_PESSOAS)
            .peso(UPDATED_PESO);

        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getOrigem()).isEqualTo(UPDATED_ORIGEM);
        assertThat(testSolicitacao.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testSolicitacao.getDataSolicitacao()).isEqualTo(UPDATED_DATA_SOLICITACAO);
        assertThat(testSolicitacao.getHorarioSaida()).isEqualTo(UPDATED_HORARIO_SAIDA);
        assertThat(testSolicitacao.getHorarioRetorno()).isEqualTo(UPDATED_HORARIO_RETORNO);
        assertThat(testSolicitacao.getDistanciaEstimadaKm()).isEqualTo(UPDATED_DISTANCIA_ESTIMADA_KM);
        assertThat(testSolicitacao.getJustificativa()).isEqualTo(UPDATED_JUSTIFICATIVA);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getnPessoas()).isEqualTo(UPDATED_N_PESSOAS);
        assertThat(testSolicitacao.getPeso()).isEqualTo(UPDATED_PESO);
    }

    @Test
    @Transactional
    void patchNonExistingSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeDelete = solicitacaoRepository.findAll().size();

        // Delete the solicitacao
        restSolicitacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, solicitacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
