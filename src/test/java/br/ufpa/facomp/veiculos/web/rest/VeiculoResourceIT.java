package br.ufpa.facomp.veiculos.web.rest;

import static br.ufpa.facomp.veiculos.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import br.ufpa.facomp.veiculos.domain.Fabricante;
import br.ufpa.facomp.veiculos.domain.Usuario;
import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.repository.VeiculoRepository;
import br.ufpa.facomp.veiculos.service.VeiculoService;
import br.ufpa.facomp.veiculos.service.criteria.VeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.VeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.VeiculoMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VeiculoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VeiculoResourceIT {

    private static final String DEFAULT_PLACA = "AAAAAAAAAA";
    private static final String UPDATED_PLACA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO = 1;
    private static final Integer UPDATED_ANO = 2;
    private static final Integer SMALLER_ANO = 1 - 1;

    private static final Boolean DEFAULT_DISPONIVEL = false;
    private static final Boolean UPDATED_DISPONIVEL = true;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Mock
    private VeiculoRepository veiculoRepositoryMock;

    @Autowired
    private VeiculoMapper veiculoMapper;

    @Mock
    private VeiculoService veiculoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVeiculoMockMvc;

    private Veiculo veiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veiculo createEntity(EntityManager em) {
        Veiculo veiculo = new Veiculo()
            .placa(DEFAULT_PLACA)
            .modelo(DEFAULT_MODELO)
            .ano(DEFAULT_ANO)
            .disponivel(DEFAULT_DISPONIVEL)
            .criado(DEFAULT_CRIADO);
        return veiculo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veiculo createUpdatedEntity(EntityManager em) {
        Veiculo veiculo = new Veiculo()
            .placa(UPDATED_PLACA)
            .modelo(UPDATED_MODELO)
            .ano(UPDATED_ANO)
            .disponivel(UPDATED_DISPONIVEL)
            .criado(UPDATED_CRIADO);
        return veiculo;
    }

    @BeforeEach
    public void initTest() {
        veiculo = createEntity(em);
    }

    @Test
    @Transactional
    void createVeiculo() throws Exception {
        int databaseSizeBeforeCreate = veiculoRepository.findAll().size();
        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);
        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(veiculoDTO)))
            .andExpect(status().isCreated());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getPlaca()).isEqualTo(DEFAULT_PLACA);
        assertThat(testVeiculo.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testVeiculo.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testVeiculo.getDisponivel()).isEqualTo(DEFAULT_DISPONIVEL);
        assertThat(testVeiculo.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createVeiculoWithExistingId() throws Exception {
        // Create the Veiculo with an existing ID
        veiculo.setId(1L);
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        int databaseSizeBeforeCreate = veiculoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(veiculoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlacaIsRequired() throws Exception {
        int databaseSizeBeforeTest = veiculoRepository.findAll().size();
        // set the field null
        veiculo.setPlaca(null);

        // Create the Veiculo, which fails.
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(veiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVeiculos() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].disponivel").value(hasItem(DEFAULT_DISPONIVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVeiculosWithEagerRelationshipsIsEnabled() throws Exception {
        when(veiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(veiculoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVeiculosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(veiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(veiculoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get the veiculo
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, veiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(veiculo.getId().intValue()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO))
            .andExpect(jsonPath("$.disponivel").value(DEFAULT_DISPONIVEL.booleanValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getVeiculosByIdFiltering() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        Long id = veiculo.getId();

        defaultVeiculoShouldBeFound("id.equals=" + id);
        defaultVeiculoShouldNotBeFound("id.notEquals=" + id);

        defaultVeiculoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVeiculoShouldNotBeFound("id.greaterThan=" + id);

        defaultVeiculoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVeiculoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa equals to DEFAULT_PLACA
        defaultVeiculoShouldBeFound("placa.equals=" + DEFAULT_PLACA);

        // Get all the veiculoList where placa equals to UPDATED_PLACA
        defaultVeiculoShouldNotBeFound("placa.equals=" + UPDATED_PLACA);
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa not equals to DEFAULT_PLACA
        defaultVeiculoShouldNotBeFound("placa.notEquals=" + DEFAULT_PLACA);

        // Get all the veiculoList where placa not equals to UPDATED_PLACA
        defaultVeiculoShouldBeFound("placa.notEquals=" + UPDATED_PLACA);
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaIsInShouldWork() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa in DEFAULT_PLACA or UPDATED_PLACA
        defaultVeiculoShouldBeFound("placa.in=" + DEFAULT_PLACA + "," + UPDATED_PLACA);

        // Get all the veiculoList where placa equals to UPDATED_PLACA
        defaultVeiculoShouldNotBeFound("placa.in=" + UPDATED_PLACA);
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaIsNullOrNotNull() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa is not null
        defaultVeiculoShouldBeFound("placa.specified=true");

        // Get all the veiculoList where placa is null
        defaultVeiculoShouldNotBeFound("placa.specified=false");
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaContainsSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa contains DEFAULT_PLACA
        defaultVeiculoShouldBeFound("placa.contains=" + DEFAULT_PLACA);

        // Get all the veiculoList where placa contains UPDATED_PLACA
        defaultVeiculoShouldNotBeFound("placa.contains=" + UPDATED_PLACA);
    }

    @Test
    @Transactional
    void getAllVeiculosByPlacaNotContainsSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where placa does not contain DEFAULT_PLACA
        defaultVeiculoShouldNotBeFound("placa.doesNotContain=" + DEFAULT_PLACA);

        // Get all the veiculoList where placa does not contain UPDATED_PLACA
        defaultVeiculoShouldBeFound("placa.doesNotContain=" + UPDATED_PLACA);
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo equals to DEFAULT_MODELO
        defaultVeiculoShouldBeFound("modelo.equals=" + DEFAULT_MODELO);

        // Get all the veiculoList where modelo equals to UPDATED_MODELO
        defaultVeiculoShouldNotBeFound("modelo.equals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo not equals to DEFAULT_MODELO
        defaultVeiculoShouldNotBeFound("modelo.notEquals=" + DEFAULT_MODELO);

        // Get all the veiculoList where modelo not equals to UPDATED_MODELO
        defaultVeiculoShouldBeFound("modelo.notEquals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloIsInShouldWork() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo in DEFAULT_MODELO or UPDATED_MODELO
        defaultVeiculoShouldBeFound("modelo.in=" + DEFAULT_MODELO + "," + UPDATED_MODELO);

        // Get all the veiculoList where modelo equals to UPDATED_MODELO
        defaultVeiculoShouldNotBeFound("modelo.in=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloIsNullOrNotNull() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo is not null
        defaultVeiculoShouldBeFound("modelo.specified=true");

        // Get all the veiculoList where modelo is null
        defaultVeiculoShouldNotBeFound("modelo.specified=false");
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloContainsSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo contains DEFAULT_MODELO
        defaultVeiculoShouldBeFound("modelo.contains=" + DEFAULT_MODELO);

        // Get all the veiculoList where modelo contains UPDATED_MODELO
        defaultVeiculoShouldNotBeFound("modelo.contains=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVeiculosByModeloNotContainsSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where modelo does not contain DEFAULT_MODELO
        defaultVeiculoShouldNotBeFound("modelo.doesNotContain=" + DEFAULT_MODELO);

        // Get all the veiculoList where modelo does not contain UPDATED_MODELO
        defaultVeiculoShouldBeFound("modelo.doesNotContain=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano equals to DEFAULT_ANO
        defaultVeiculoShouldBeFound("ano.equals=" + DEFAULT_ANO);

        // Get all the veiculoList where ano equals to UPDATED_ANO
        defaultVeiculoShouldNotBeFound("ano.equals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano not equals to DEFAULT_ANO
        defaultVeiculoShouldNotBeFound("ano.notEquals=" + DEFAULT_ANO);

        // Get all the veiculoList where ano not equals to UPDATED_ANO
        defaultVeiculoShouldBeFound("ano.notEquals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsInShouldWork() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano in DEFAULT_ANO or UPDATED_ANO
        defaultVeiculoShouldBeFound("ano.in=" + DEFAULT_ANO + "," + UPDATED_ANO);

        // Get all the veiculoList where ano equals to UPDATED_ANO
        defaultVeiculoShouldNotBeFound("ano.in=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano is not null
        defaultVeiculoShouldBeFound("ano.specified=true");

        // Get all the veiculoList where ano is null
        defaultVeiculoShouldNotBeFound("ano.specified=false");
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano is greater than or equal to DEFAULT_ANO
        defaultVeiculoShouldBeFound("ano.greaterThanOrEqual=" + DEFAULT_ANO);

        // Get all the veiculoList where ano is greater than or equal to UPDATED_ANO
        defaultVeiculoShouldNotBeFound("ano.greaterThanOrEqual=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano is less than or equal to DEFAULT_ANO
        defaultVeiculoShouldBeFound("ano.lessThanOrEqual=" + DEFAULT_ANO);

        // Get all the veiculoList where ano is less than or equal to SMALLER_ANO
        defaultVeiculoShouldNotBeFound("ano.lessThanOrEqual=" + SMALLER_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsLessThanSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano is less than DEFAULT_ANO
        defaultVeiculoShouldNotBeFound("ano.lessThan=" + DEFAULT_ANO);

        // Get all the veiculoList where ano is less than UPDATED_ANO
        defaultVeiculoShouldBeFound("ano.lessThan=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByAnoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where ano is greater than DEFAULT_ANO
        defaultVeiculoShouldNotBeFound("ano.greaterThan=" + DEFAULT_ANO);

        // Get all the veiculoList where ano is greater than SMALLER_ANO
        defaultVeiculoShouldBeFound("ano.greaterThan=" + SMALLER_ANO);
    }

    @Test
    @Transactional
    void getAllVeiculosByDisponivelIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where disponivel equals to DEFAULT_DISPONIVEL
        defaultVeiculoShouldBeFound("disponivel.equals=" + DEFAULT_DISPONIVEL);

        // Get all the veiculoList where disponivel equals to UPDATED_DISPONIVEL
        defaultVeiculoShouldNotBeFound("disponivel.equals=" + UPDATED_DISPONIVEL);
    }

    @Test
    @Transactional
    void getAllVeiculosByDisponivelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where disponivel not equals to DEFAULT_DISPONIVEL
        defaultVeiculoShouldNotBeFound("disponivel.notEquals=" + DEFAULT_DISPONIVEL);

        // Get all the veiculoList where disponivel not equals to UPDATED_DISPONIVEL
        defaultVeiculoShouldBeFound("disponivel.notEquals=" + UPDATED_DISPONIVEL);
    }

    @Test
    @Transactional
    void getAllVeiculosByDisponivelIsInShouldWork() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where disponivel in DEFAULT_DISPONIVEL or UPDATED_DISPONIVEL
        defaultVeiculoShouldBeFound("disponivel.in=" + DEFAULT_DISPONIVEL + "," + UPDATED_DISPONIVEL);

        // Get all the veiculoList where disponivel equals to UPDATED_DISPONIVEL
        defaultVeiculoShouldNotBeFound("disponivel.in=" + UPDATED_DISPONIVEL);
    }

    @Test
    @Transactional
    void getAllVeiculosByDisponivelIsNullOrNotNull() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where disponivel is not null
        defaultVeiculoShouldBeFound("disponivel.specified=true");

        // Get all the veiculoList where disponivel is null
        defaultVeiculoShouldNotBeFound("disponivel.specified=false");
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado equals to DEFAULT_CRIADO
        defaultVeiculoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado equals to UPDATED_CRIADO
        defaultVeiculoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado not equals to DEFAULT_CRIADO
        defaultVeiculoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado not equals to UPDATED_CRIADO
        defaultVeiculoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultVeiculoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the veiculoList where criado equals to UPDATED_CRIADO
        defaultVeiculoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado is not null
        defaultVeiculoShouldBeFound("criado.specified=true");

        // Get all the veiculoList where criado is null
        defaultVeiculoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultVeiculoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado is greater than or equal to UPDATED_CRIADO
        defaultVeiculoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado is less than or equal to DEFAULT_CRIADO
        defaultVeiculoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado is less than or equal to SMALLER_CRIADO
        defaultVeiculoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado is less than DEFAULT_CRIADO
        defaultVeiculoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado is less than UPDATED_CRIADO
        defaultVeiculoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList where criado is greater than DEFAULT_CRIADO
        defaultVeiculoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the veiculoList where criado is greater than SMALLER_CRIADO
        defaultVeiculoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllVeiculosByFabricanteIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        Fabricante fabricante = FabricanteResourceIT.createEntity(em);
        em.persist(fabricante);
        em.flush();
        veiculo.setFabricante(fabricante);
        veiculoRepository.saveAndFlush(veiculo);
        Long fabricanteId = fabricante.getId();

        // Get all the veiculoList where fabricante equals to fabricanteId
        defaultVeiculoShouldBeFound("fabricanteId.equals=" + fabricanteId);

        // Get all the veiculoList where fabricante equals to (fabricanteId + 1)
        defaultVeiculoShouldNotBeFound("fabricanteId.equals=" + (fabricanteId + 1));
    }

    @Test
    @Transactional
    void getAllVeiculosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        CategoriaVeiculo categoria = CategoriaVeiculoResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        veiculo.setCategoria(categoria);
        veiculoRepository.saveAndFlush(veiculo);
        Long categoriaId = categoria.getId();

        // Get all the veiculoList where categoria equals to categoriaId
        defaultVeiculoShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the veiculoList where categoria equals to (categoriaId + 1)
        defaultVeiculoShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllVeiculosByMotoristasHabilitadosIsEqualToSomething() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        Usuario motoristasHabilitados = UsuarioResourceIT.createEntity(em);
        em.persist(motoristasHabilitados);
        em.flush();
        veiculo.addMotoristasHabilitados(motoristasHabilitados);
        veiculoRepository.saveAndFlush(veiculo);
        Long motoristasHabilitadosId = motoristasHabilitados.getId();

        // Get all the veiculoList where motoristasHabilitados equals to motoristasHabilitadosId
        defaultVeiculoShouldBeFound("motoristasHabilitadosId.equals=" + motoristasHabilitadosId);

        // Get all the veiculoList where motoristasHabilitados equals to (motoristasHabilitadosId + 1)
        defaultVeiculoShouldNotBeFound("motoristasHabilitadosId.equals=" + (motoristasHabilitadosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVeiculoShouldBeFound(String filter) throws Exception {
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].disponivel").value(hasItem(DEFAULT_DISPONIVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVeiculoShouldNotBeFound(String filter) throws Exception {
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVeiculo() throws Exception {
        // Get the veiculo
        restVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();

        // Update the veiculo
        Veiculo updatedVeiculo = veiculoRepository.findById(veiculo.getId()).get();
        // Disconnect from session so that the updates on updatedVeiculo are not directly saved in db
        em.detach(updatedVeiculo);
        updatedVeiculo.placa(UPDATED_PLACA).modelo(UPDATED_MODELO).ano(UPDATED_ANO).disponivel(UPDATED_DISPONIVEL).criado(UPDATED_CRIADO);
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(updatedVeiculo);

        restVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, veiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getPlaca()).isEqualTo(UPDATED_PLACA);
        assertThat(testVeiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVeiculo.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testVeiculo.getDisponivel()).isEqualTo(UPDATED_DISPONIVEL);
        assertThat(testVeiculo.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, veiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(veiculoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVeiculoWithPatch() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();

        // Update the veiculo using partial update
        Veiculo partialUpdatedVeiculo = new Veiculo();
        partialUpdatedVeiculo.setId(veiculo.getId());

        partialUpdatedVeiculo.disponivel(UPDATED_DISPONIVEL);

        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getPlaca()).isEqualTo(DEFAULT_PLACA);
        assertThat(testVeiculo.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testVeiculo.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testVeiculo.getDisponivel()).isEqualTo(UPDATED_DISPONIVEL);
        assertThat(testVeiculo.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateVeiculoWithPatch() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();

        // Update the veiculo using partial update
        Veiculo partialUpdatedVeiculo = new Veiculo();
        partialUpdatedVeiculo.setId(veiculo.getId());

        partialUpdatedVeiculo
            .placa(UPDATED_PLACA)
            .modelo(UPDATED_MODELO)
            .ano(UPDATED_ANO)
            .disponivel(UPDATED_DISPONIVEL)
            .criado(UPDATED_CRIADO);

        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getPlaca()).isEqualTo(UPDATED_PLACA);
        assertThat(testVeiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVeiculo.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testVeiculo.getDisponivel()).isEqualTo(UPDATED_DISPONIVEL);
        assertThat(testVeiculo.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, veiculoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();
        veiculo.setId(count.incrementAndGet());

        // Create the Veiculo
        VeiculoDTO veiculoDTO = veiculoMapper.toDto(veiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(veiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        int databaseSizeBeforeDelete = veiculoRepository.findAll().size();

        // Delete the veiculo
        restVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, veiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
