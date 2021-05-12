package br.ufpa.facomp.veiculos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.CategoriaVeiculo;
import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import br.ufpa.facomp.veiculos.repository.CategoriaVeiculoRepository;
import br.ufpa.facomp.veiculos.service.criteria.CategoriaVeiculoCriteria;
import br.ufpa.facomp.veiculos.service.dto.CategoriaVeiculoDTO;
import br.ufpa.facomp.veiculos.service.mapper.CategoriaVeiculoMapper;
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
 * Integration tests for the {@link CategoriaVeiculoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaVeiculoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDADE_PESSOAS = 1;
    private static final Integer UPDATED_CAPACIDADE_PESSOAS = 2;
    private static final Integer SMALLER_CAPACIDADE_PESSOAS = 1 - 1;

    private static final Double DEFAULT_CAPACIDADE_PESO = 1D;
    private static final Double UPDATED_CAPACIDADE_PESO = 2D;
    private static final Double SMALLER_CAPACIDADE_PESO = 1D - 1D;

    private static final Double DEFAULT_CAPACIDADE_AREA = 1D;
    private static final Double UPDATED_CAPACIDADE_AREA = 2D;
    private static final Double SMALLER_CAPACIDADE_AREA = 1D - 1D;

    private static final Integer DEFAULT_EIXOS = 1;
    private static final Integer UPDATED_EIXOS = 2;
    private static final Integer SMALLER_EIXOS = 1 - 1;

    private static final Double DEFAULT_ALTURA = 1D;
    private static final Double UPDATED_ALTURA = 2D;
    private static final Double SMALLER_ALTURA = 1D - 1D;

    private static final NivelCNH DEFAULT_NIVEL_CNH = NivelCNH.A;
    private static final NivelCNH UPDATED_NIVEL_CNH = NivelCNH.B;

    private static final String ENTITY_API_URL = "/api/categoria-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaVeiculoRepository categoriaVeiculoRepository;

    @Autowired
    private CategoriaVeiculoMapper categoriaVeiculoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaVeiculoMockMvc;

    private CategoriaVeiculo categoriaVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaVeiculo createEntity(EntityManager em) {
        CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo()
            .nome(DEFAULT_NOME)
            .capacidadePessoas(DEFAULT_CAPACIDADE_PESSOAS)
            .capacidadePeso(DEFAULT_CAPACIDADE_PESO)
            .capacidadeArea(DEFAULT_CAPACIDADE_AREA)
            .eixos(DEFAULT_EIXOS)
            .altura(DEFAULT_ALTURA)
            .nivelCNH(DEFAULT_NIVEL_CNH);
        return categoriaVeiculo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaVeiculo createUpdatedEntity(EntityManager em) {
        CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo()
            .nome(UPDATED_NOME)
            .capacidadePessoas(UPDATED_CAPACIDADE_PESSOAS)
            .capacidadePeso(UPDATED_CAPACIDADE_PESO)
            .capacidadeArea(UPDATED_CAPACIDADE_AREA)
            .eixos(UPDATED_EIXOS)
            .altura(UPDATED_ALTURA)
            .nivelCNH(UPDATED_NIVEL_CNH);
        return categoriaVeiculo;
    }

    @BeforeEach
    public void initTest() {
        categoriaVeiculo = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeCreate = categoriaVeiculoRepository.findAll().size();
        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);
        restCategoriaVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCategoriaVeiculo.getCapacidadePessoas()).isEqualTo(DEFAULT_CAPACIDADE_PESSOAS);
        assertThat(testCategoriaVeiculo.getCapacidadePeso()).isEqualTo(DEFAULT_CAPACIDADE_PESO);
        assertThat(testCategoriaVeiculo.getCapacidadeArea()).isEqualTo(DEFAULT_CAPACIDADE_AREA);
        assertThat(testCategoriaVeiculo.getEixos()).isEqualTo(DEFAULT_EIXOS);
        assertThat(testCategoriaVeiculo.getAltura()).isEqualTo(DEFAULT_ALTURA);
        assertThat(testCategoriaVeiculo.getNivelCNH()).isEqualTo(DEFAULT_NIVEL_CNH);
    }

    @Test
    @Transactional
    void createCategoriaVeiculoWithExistingId() throws Exception {
        // Create the CategoriaVeiculo with an existing ID
        categoriaVeiculo.setId(1L);
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        int databaseSizeBeforeCreate = categoriaVeiculoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculos() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].capacidadePessoas").value(hasItem(DEFAULT_CAPACIDADE_PESSOAS)))
            .andExpect(jsonPath("$.[*].capacidadePeso").value(hasItem(DEFAULT_CAPACIDADE_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].capacidadeArea").value(hasItem(DEFAULT_CAPACIDADE_AREA.doubleValue())))
            .andExpect(jsonPath("$.[*].eixos").value(hasItem(DEFAULT_EIXOS)))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(DEFAULT_ALTURA.doubleValue())))
            .andExpect(jsonPath("$.[*].nivelCNH").value(hasItem(DEFAULT_NIVEL_CNH.toString())));
    }

    @Test
    @Transactional
    void getCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get the categoriaVeiculo
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaVeiculo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.capacidadePessoas").value(DEFAULT_CAPACIDADE_PESSOAS))
            .andExpect(jsonPath("$.capacidadePeso").value(DEFAULT_CAPACIDADE_PESO.doubleValue()))
            .andExpect(jsonPath("$.capacidadeArea").value(DEFAULT_CAPACIDADE_AREA.doubleValue()))
            .andExpect(jsonPath("$.eixos").value(DEFAULT_EIXOS))
            .andExpect(jsonPath("$.altura").value(DEFAULT_ALTURA.doubleValue()))
            .andExpect(jsonPath("$.nivelCNH").value(DEFAULT_NIVEL_CNH.toString()));
    }

    @Test
    @Transactional
    void getCategoriaVeiculosByIdFiltering() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        Long id = categoriaVeiculo.getId();

        defaultCategoriaVeiculoShouldBeFound("id.equals=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaVeiculoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaVeiculoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome equals to DEFAULT_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome equals to UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome not equals to DEFAULT_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome not equals to UPDATED_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the categoriaVeiculoList where nome equals to UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome is not null
        defaultCategoriaVeiculoShouldBeFound("nome.specified=true");

        // Get all the categoriaVeiculoList where nome is null
        defaultCategoriaVeiculoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeContainsSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome contains DEFAULT_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome contains UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome does not contain DEFAULT_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome does not contain UPDATED_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas equals to DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.equals=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas equals to UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.equals=" + UPDATED_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas not equals to DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.notEquals=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas not equals to UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.notEquals=" + UPDATED_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas in DEFAULT_CAPACIDADE_PESSOAS or UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.in=" + DEFAULT_CAPACIDADE_PESSOAS + "," + UPDATED_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas equals to UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.in=" + UPDATED_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas is not null
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.specified=true");

        // Get all the categoriaVeiculoList where capacidadePessoas is null
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas is greater than or equal to DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.greaterThanOrEqual=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas is greater than or equal to UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.greaterThanOrEqual=" + UPDATED_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas is less than or equal to DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.lessThanOrEqual=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas is less than or equal to SMALLER_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.lessThanOrEqual=" + SMALLER_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas is less than DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.lessThan=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas is less than UPDATED_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.lessThan=" + UPDATED_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePessoasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePessoas is greater than DEFAULT_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePessoas.greaterThan=" + DEFAULT_CAPACIDADE_PESSOAS);

        // Get all the categoriaVeiculoList where capacidadePessoas is greater than SMALLER_CAPACIDADE_PESSOAS
        defaultCategoriaVeiculoShouldBeFound("capacidadePessoas.greaterThan=" + SMALLER_CAPACIDADE_PESSOAS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso equals to DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.equals=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso equals to UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.equals=" + UPDATED_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso not equals to DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.notEquals=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso not equals to UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.notEquals=" + UPDATED_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso in DEFAULT_CAPACIDADE_PESO or UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.in=" + DEFAULT_CAPACIDADE_PESO + "," + UPDATED_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso equals to UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.in=" + UPDATED_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso is not null
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.specified=true");

        // Get all the categoriaVeiculoList where capacidadePeso is null
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso is greater than or equal to DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.greaterThanOrEqual=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso is greater than or equal to UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.greaterThanOrEqual=" + UPDATED_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso is less than or equal to DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.lessThanOrEqual=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso is less than or equal to SMALLER_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.lessThanOrEqual=" + SMALLER_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso is less than DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.lessThan=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso is less than UPDATED_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.lessThan=" + UPDATED_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadePesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadePeso is greater than DEFAULT_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldNotBeFound("capacidadePeso.greaterThan=" + DEFAULT_CAPACIDADE_PESO);

        // Get all the categoriaVeiculoList where capacidadePeso is greater than SMALLER_CAPACIDADE_PESO
        defaultCategoriaVeiculoShouldBeFound("capacidadePeso.greaterThan=" + SMALLER_CAPACIDADE_PESO);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea equals to DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.equals=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea equals to UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.equals=" + UPDATED_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea not equals to DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.notEquals=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea not equals to UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.notEquals=" + UPDATED_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea in DEFAULT_CAPACIDADE_AREA or UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.in=" + DEFAULT_CAPACIDADE_AREA + "," + UPDATED_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea equals to UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.in=" + UPDATED_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea is not null
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.specified=true");

        // Get all the categoriaVeiculoList where capacidadeArea is null
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea is greater than or equal to DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.greaterThanOrEqual=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea is greater than or equal to UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.greaterThanOrEqual=" + UPDATED_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea is less than or equal to DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.lessThanOrEqual=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea is less than or equal to SMALLER_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.lessThanOrEqual=" + SMALLER_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea is less than DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.lessThan=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea is less than UPDATED_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.lessThan=" + UPDATED_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByCapacidadeAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where capacidadeArea is greater than DEFAULT_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldNotBeFound("capacidadeArea.greaterThan=" + DEFAULT_CAPACIDADE_AREA);

        // Get all the categoriaVeiculoList where capacidadeArea is greater than SMALLER_CAPACIDADE_AREA
        defaultCategoriaVeiculoShouldBeFound("capacidadeArea.greaterThan=" + SMALLER_CAPACIDADE_AREA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos equals to DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.equals=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos equals to UPDATED_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.equals=" + UPDATED_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos not equals to DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.notEquals=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos not equals to UPDATED_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.notEquals=" + UPDATED_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos in DEFAULT_EIXOS or UPDATED_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.in=" + DEFAULT_EIXOS + "," + UPDATED_EIXOS);

        // Get all the categoriaVeiculoList where eixos equals to UPDATED_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.in=" + UPDATED_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos is not null
        defaultCategoriaVeiculoShouldBeFound("eixos.specified=true");

        // Get all the categoriaVeiculoList where eixos is null
        defaultCategoriaVeiculoShouldNotBeFound("eixos.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos is greater than or equal to DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.greaterThanOrEqual=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos is greater than or equal to UPDATED_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.greaterThanOrEqual=" + UPDATED_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos is less than or equal to DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.lessThanOrEqual=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos is less than or equal to SMALLER_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.lessThanOrEqual=" + SMALLER_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos is less than DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.lessThan=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos is less than UPDATED_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.lessThan=" + UPDATED_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByEixosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where eixos is greater than DEFAULT_EIXOS
        defaultCategoriaVeiculoShouldNotBeFound("eixos.greaterThan=" + DEFAULT_EIXOS);

        // Get all the categoriaVeiculoList where eixos is greater than SMALLER_EIXOS
        defaultCategoriaVeiculoShouldBeFound("eixos.greaterThan=" + SMALLER_EIXOS);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura equals to DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.equals=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura equals to UPDATED_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.equals=" + UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura not equals to DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.notEquals=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura not equals to UPDATED_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.notEquals=" + UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura in DEFAULT_ALTURA or UPDATED_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.in=" + DEFAULT_ALTURA + "," + UPDATED_ALTURA);

        // Get all the categoriaVeiculoList where altura equals to UPDATED_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.in=" + UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura is not null
        defaultCategoriaVeiculoShouldBeFound("altura.specified=true");

        // Get all the categoriaVeiculoList where altura is null
        defaultCategoriaVeiculoShouldNotBeFound("altura.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura is greater than or equal to DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.greaterThanOrEqual=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura is greater than or equal to UPDATED_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.greaterThanOrEqual=" + UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura is less than or equal to DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.lessThanOrEqual=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura is less than or equal to SMALLER_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.lessThanOrEqual=" + SMALLER_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura is less than DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.lessThan=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura is less than UPDATED_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.lessThan=" + UPDATED_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByAlturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where altura is greater than DEFAULT_ALTURA
        defaultCategoriaVeiculoShouldNotBeFound("altura.greaterThan=" + DEFAULT_ALTURA);

        // Get all the categoriaVeiculoList where altura is greater than SMALLER_ALTURA
        defaultCategoriaVeiculoShouldBeFound("altura.greaterThan=" + SMALLER_ALTURA);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNivelCNHIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nivelCNH equals to DEFAULT_NIVEL_CNH
        defaultCategoriaVeiculoShouldBeFound("nivelCNH.equals=" + DEFAULT_NIVEL_CNH);

        // Get all the categoriaVeiculoList where nivelCNH equals to UPDATED_NIVEL_CNH
        defaultCategoriaVeiculoShouldNotBeFound("nivelCNH.equals=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNivelCNHIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nivelCNH not equals to DEFAULT_NIVEL_CNH
        defaultCategoriaVeiculoShouldNotBeFound("nivelCNH.notEquals=" + DEFAULT_NIVEL_CNH);

        // Get all the categoriaVeiculoList where nivelCNH not equals to UPDATED_NIVEL_CNH
        defaultCategoriaVeiculoShouldBeFound("nivelCNH.notEquals=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNivelCNHIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nivelCNH in DEFAULT_NIVEL_CNH or UPDATED_NIVEL_CNH
        defaultCategoriaVeiculoShouldBeFound("nivelCNH.in=" + DEFAULT_NIVEL_CNH + "," + UPDATED_NIVEL_CNH);

        // Get all the categoriaVeiculoList where nivelCNH equals to UPDATED_NIVEL_CNH
        defaultCategoriaVeiculoShouldNotBeFound("nivelCNH.in=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNivelCNHIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nivelCNH is not null
        defaultCategoriaVeiculoShouldBeFound("nivelCNH.specified=true");

        // Get all the categoriaVeiculoList where nivelCNH is null
        defaultCategoriaVeiculoShouldNotBeFound("nivelCNH.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaVeiculoShouldBeFound(String filter) throws Exception {
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].capacidadePessoas").value(hasItem(DEFAULT_CAPACIDADE_PESSOAS)))
            .andExpect(jsonPath("$.[*].capacidadePeso").value(hasItem(DEFAULT_CAPACIDADE_PESO.doubleValue())))
            .andExpect(jsonPath("$.[*].capacidadeArea").value(hasItem(DEFAULT_CAPACIDADE_AREA.doubleValue())))
            .andExpect(jsonPath("$.[*].eixos").value(hasItem(DEFAULT_EIXOS)))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(DEFAULT_ALTURA.doubleValue())))
            .andExpect(jsonPath("$.[*].nivelCNH").value(hasItem(DEFAULT_NIVEL_CNH.toString())));

        // Check, that the count call also returns 1
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaVeiculoShouldNotBeFound(String filter) throws Exception {
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaVeiculo() throws Exception {
        // Get the categoriaVeiculo
        restCategoriaVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo
        CategoriaVeiculo updatedCategoriaVeiculo = categoriaVeiculoRepository.findById(categoriaVeiculo.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaVeiculo are not directly saved in db
        em.detach(updatedCategoriaVeiculo);
        updatedCategoriaVeiculo
            .nome(UPDATED_NOME)
            .capacidadePessoas(UPDATED_CAPACIDADE_PESSOAS)
            .capacidadePeso(UPDATED_CAPACIDADE_PESO)
            .capacidadeArea(UPDATED_CAPACIDADE_AREA)
            .eixos(UPDATED_EIXOS)
            .altura(UPDATED_ALTURA)
            .nivelCNH(UPDATED_NIVEL_CNH);
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(updatedCategoriaVeiculo);

        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoriaVeiculo.getCapacidadePessoas()).isEqualTo(UPDATED_CAPACIDADE_PESSOAS);
        assertThat(testCategoriaVeiculo.getCapacidadePeso()).isEqualTo(UPDATED_CAPACIDADE_PESO);
        assertThat(testCategoriaVeiculo.getCapacidadeArea()).isEqualTo(UPDATED_CAPACIDADE_AREA);
        assertThat(testCategoriaVeiculo.getEixos()).isEqualTo(UPDATED_EIXOS);
        assertThat(testCategoriaVeiculo.getAltura()).isEqualTo(UPDATED_ALTURA);
        assertThat(testCategoriaVeiculo.getNivelCNH()).isEqualTo(UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaVeiculoWithPatch() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo using partial update
        CategoriaVeiculo partialUpdatedCategoriaVeiculo = new CategoriaVeiculo();
        partialUpdatedCategoriaVeiculo.setId(categoriaVeiculo.getId());

        partialUpdatedCategoriaVeiculo
            .capacidadePessoas(UPDATED_CAPACIDADE_PESSOAS)
            .capacidadePeso(UPDATED_CAPACIDADE_PESO)
            .capacidadeArea(UPDATED_CAPACIDADE_AREA);

        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCategoriaVeiculo.getCapacidadePessoas()).isEqualTo(UPDATED_CAPACIDADE_PESSOAS);
        assertThat(testCategoriaVeiculo.getCapacidadePeso()).isEqualTo(UPDATED_CAPACIDADE_PESO);
        assertThat(testCategoriaVeiculo.getCapacidadeArea()).isEqualTo(UPDATED_CAPACIDADE_AREA);
        assertThat(testCategoriaVeiculo.getEixos()).isEqualTo(DEFAULT_EIXOS);
        assertThat(testCategoriaVeiculo.getAltura()).isEqualTo(DEFAULT_ALTURA);
        assertThat(testCategoriaVeiculo.getNivelCNH()).isEqualTo(DEFAULT_NIVEL_CNH);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaVeiculoWithPatch() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo using partial update
        CategoriaVeiculo partialUpdatedCategoriaVeiculo = new CategoriaVeiculo();
        partialUpdatedCategoriaVeiculo.setId(categoriaVeiculo.getId());

        partialUpdatedCategoriaVeiculo
            .nome(UPDATED_NOME)
            .capacidadePessoas(UPDATED_CAPACIDADE_PESSOAS)
            .capacidadePeso(UPDATED_CAPACIDADE_PESO)
            .capacidadeArea(UPDATED_CAPACIDADE_AREA)
            .eixos(UPDATED_EIXOS)
            .altura(UPDATED_ALTURA)
            .nivelCNH(UPDATED_NIVEL_CNH);

        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoriaVeiculo.getCapacidadePessoas()).isEqualTo(UPDATED_CAPACIDADE_PESSOAS);
        assertThat(testCategoriaVeiculo.getCapacidadePeso()).isEqualTo(UPDATED_CAPACIDADE_PESO);
        assertThat(testCategoriaVeiculo.getCapacidadeArea()).isEqualTo(UPDATED_CAPACIDADE_AREA);
        assertThat(testCategoriaVeiculo.getEixos()).isEqualTo(UPDATED_EIXOS);
        assertThat(testCategoriaVeiculo.getAltura()).isEqualTo(UPDATED_ALTURA);
        assertThat(testCategoriaVeiculo.getNivelCNH()).isEqualTo(UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeDelete = categoriaVeiculoRepository.findAll().size();

        // Delete the categoriaVeiculo
        restCategoriaVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
