package br.ufpa.facomp.veiculos.web.rest;

import static br.ufpa.facomp.veiculos.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.facomp.veiculos.IntegrationTest;
import br.ufpa.facomp.veiculos.domain.Solicitacao;
import br.ufpa.facomp.veiculos.domain.Usuario;
import br.ufpa.facomp.veiculos.domain.Veiculo;
import br.ufpa.facomp.veiculos.domain.enumeration.Identificador;
import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import br.ufpa.facomp.veiculos.domain.enumeration.PerfilUsuario;
import br.ufpa.facomp.veiculos.repository.UsuarioRepository;
import br.ufpa.facomp.veiculos.service.criteria.UsuarioCriteria;
import br.ufpa.facomp.veiculos.service.dto.UsuarioDTO;
import br.ufpa.facomp.veiculos.service.mapper.UsuarioMapper;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final PerfilUsuario DEFAULT_PERFIL = PerfilUsuario.MOTORISTA;
    private static final PerfilUsuario UPDATED_PERFIL = PerfilUsuario.SETOR_TRANSPORTE;

    private static final Identificador DEFAULT_IDENTIFICACAO = Identificador.SIAPE;
    private static final Identificador UPDATED_IDENTIFICACAO = Identificador.MATRICULA_SIGAA;

    private static final String DEFAULT_NIDENTIFICAO = "AAAAAAAAAA";
    private static final String UPDATED_NIDENTIFICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WHATSAPP = false;
    private static final Boolean UPDATED_WHATSAPP = true;

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final NivelCNH DEFAULT_NIVEL_CNH = NivelCNH.A;
    private static final NivelCNH UPDATED_NIVEL_CNH = NivelCNH.B;

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nome(DEFAULT_NOME)
            .perfil(DEFAULT_PERFIL)
            .identificacao(DEFAULT_IDENTIFICACAO)
            .nidentificao(DEFAULT_NIDENTIFICAO)
            .cpf(DEFAULT_CPF)
            .email(DEFAULT_EMAIL)
            .celular(DEFAULT_CELULAR)
            .whatsapp(DEFAULT_WHATSAPP)
            .ativo(DEFAULT_ATIVO)
            .criado(DEFAULT_CRIADO)
            .nivelCNH(DEFAULT_NIVEL_CNH);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nome(UPDATED_NOME)
            .perfil(UPDATED_PERFIL)
            .identificacao(UPDATED_IDENTIFICACAO)
            .nidentificao(UPDATED_NIDENTIFICAO)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .whatsapp(UPDATED_WHATSAPP)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO)
            .nivelCNH(UPDATED_NIVEL_CNH);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUsuario.getPerfil()).isEqualTo(DEFAULT_PERFIL);
        assertThat(testUsuario.getIdentificacao()).isEqualTo(DEFAULT_IDENTIFICACAO);
        assertThat(testUsuario.getNidentificao()).isEqualTo(DEFAULT_NIDENTIFICAO);
        assertThat(testUsuario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testUsuario.getWhatsapp()).isEqualTo(DEFAULT_WHATSAPP);
        assertThat(testUsuario.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testUsuario.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testUsuario.getNivelCNH()).isEqualTo(DEFAULT_NIVEL_CNH);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNome(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].perfil").value(hasItem(DEFAULT_PERFIL.toString())))
            .andExpect(jsonPath("$.[*].identificacao").value(hasItem(DEFAULT_IDENTIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].nidentificao").value(hasItem(DEFAULT_NIDENTIFICAO)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].whatsapp").value(hasItem(DEFAULT_WHATSAPP.booleanValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].nivelCNH").value(hasItem(DEFAULT_NIVEL_CNH.toString())));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.perfil").value(DEFAULT_PERFIL.toString()))
            .andExpect(jsonPath("$.identificacao").value(DEFAULT_IDENTIFICACAO.toString()))
            .andExpect(jsonPath("$.nidentificao").value(DEFAULT_NIDENTIFICAO))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.whatsapp").value(DEFAULT_WHATSAPP.booleanValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.nivelCNH").value(DEFAULT_NIVEL_CNH.toString()));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome equals to DEFAULT_NOME
        defaultUsuarioShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the usuarioList where nome equals to UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome not equals to DEFAULT_NOME
        defaultUsuarioShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the usuarioList where nome not equals to UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the usuarioList where nome equals to UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome is not null
        defaultUsuarioShouldBeFound("nome.specified=true");

        // Get all the usuarioList where nome is null
        defaultUsuarioShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome contains DEFAULT_NOME
        defaultUsuarioShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the usuarioList where nome contains UPDATED_NOME
        defaultUsuarioShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nome does not contain DEFAULT_NOME
        defaultUsuarioShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the usuarioList where nome does not contain UPDATED_NOME
        defaultUsuarioShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByPerfilIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where perfil equals to DEFAULT_PERFIL
        defaultUsuarioShouldBeFound("perfil.equals=" + DEFAULT_PERFIL);

        // Get all the usuarioList where perfil equals to UPDATED_PERFIL
        defaultUsuarioShouldNotBeFound("perfil.equals=" + UPDATED_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByPerfilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where perfil not equals to DEFAULT_PERFIL
        defaultUsuarioShouldNotBeFound("perfil.notEquals=" + DEFAULT_PERFIL);

        // Get all the usuarioList where perfil not equals to UPDATED_PERFIL
        defaultUsuarioShouldBeFound("perfil.notEquals=" + UPDATED_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByPerfilIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where perfil in DEFAULT_PERFIL or UPDATED_PERFIL
        defaultUsuarioShouldBeFound("perfil.in=" + DEFAULT_PERFIL + "," + UPDATED_PERFIL);

        // Get all the usuarioList where perfil equals to UPDATED_PERFIL
        defaultUsuarioShouldNotBeFound("perfil.in=" + UPDATED_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByPerfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where perfil is not null
        defaultUsuarioShouldBeFound("perfil.specified=true");

        // Get all the usuarioList where perfil is null
        defaultUsuarioShouldNotBeFound("perfil.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIdentificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where identificacao equals to DEFAULT_IDENTIFICACAO
        defaultUsuarioShouldBeFound("identificacao.equals=" + DEFAULT_IDENTIFICACAO);

        // Get all the usuarioList where identificacao equals to UPDATED_IDENTIFICACAO
        defaultUsuarioShouldNotBeFound("identificacao.equals=" + UPDATED_IDENTIFICACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdentificacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where identificacao not equals to DEFAULT_IDENTIFICACAO
        defaultUsuarioShouldNotBeFound("identificacao.notEquals=" + DEFAULT_IDENTIFICACAO);

        // Get all the usuarioList where identificacao not equals to UPDATED_IDENTIFICACAO
        defaultUsuarioShouldBeFound("identificacao.notEquals=" + UPDATED_IDENTIFICACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdentificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where identificacao in DEFAULT_IDENTIFICACAO or UPDATED_IDENTIFICACAO
        defaultUsuarioShouldBeFound("identificacao.in=" + DEFAULT_IDENTIFICACAO + "," + UPDATED_IDENTIFICACAO);

        // Get all the usuarioList where identificacao equals to UPDATED_IDENTIFICACAO
        defaultUsuarioShouldNotBeFound("identificacao.in=" + UPDATED_IDENTIFICACAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdentificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where identificacao is not null
        defaultUsuarioShouldBeFound("identificacao.specified=true");

        // Get all the usuarioList where identificacao is null
        defaultUsuarioShouldNotBeFound("identificacao.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao equals to DEFAULT_NIDENTIFICAO
        defaultUsuarioShouldBeFound("nidentificao.equals=" + DEFAULT_NIDENTIFICAO);

        // Get all the usuarioList where nidentificao equals to UPDATED_NIDENTIFICAO
        defaultUsuarioShouldNotBeFound("nidentificao.equals=" + UPDATED_NIDENTIFICAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao not equals to DEFAULT_NIDENTIFICAO
        defaultUsuarioShouldNotBeFound("nidentificao.notEquals=" + DEFAULT_NIDENTIFICAO);

        // Get all the usuarioList where nidentificao not equals to UPDATED_NIDENTIFICAO
        defaultUsuarioShouldBeFound("nidentificao.notEquals=" + UPDATED_NIDENTIFICAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao in DEFAULT_NIDENTIFICAO or UPDATED_NIDENTIFICAO
        defaultUsuarioShouldBeFound("nidentificao.in=" + DEFAULT_NIDENTIFICAO + "," + UPDATED_NIDENTIFICAO);

        // Get all the usuarioList where nidentificao equals to UPDATED_NIDENTIFICAO
        defaultUsuarioShouldNotBeFound("nidentificao.in=" + UPDATED_NIDENTIFICAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao is not null
        defaultUsuarioShouldBeFound("nidentificao.specified=true");

        // Get all the usuarioList where nidentificao is null
        defaultUsuarioShouldNotBeFound("nidentificao.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao contains DEFAULT_NIDENTIFICAO
        defaultUsuarioShouldBeFound("nidentificao.contains=" + DEFAULT_NIDENTIFICAO);

        // Get all the usuarioList where nidentificao contains UPDATED_NIDENTIFICAO
        defaultUsuarioShouldNotBeFound("nidentificao.contains=" + UPDATED_NIDENTIFICAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByNidentificaoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nidentificao does not contain DEFAULT_NIDENTIFICAO
        defaultUsuarioShouldNotBeFound("nidentificao.doesNotContain=" + DEFAULT_NIDENTIFICAO);

        // Get all the usuarioList where nidentificao does not contain UPDATED_NIDENTIFICAO
        defaultUsuarioShouldBeFound("nidentificao.doesNotContain=" + UPDATED_NIDENTIFICAO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf equals to DEFAULT_CPF
        defaultUsuarioShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf equals to UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf not equals to DEFAULT_CPF
        defaultUsuarioShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf not equals to UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the usuarioList where cpf equals to UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf is not null
        defaultUsuarioShouldBeFound("cpf.specified=true");

        // Get all the usuarioList where cpf is null
        defaultUsuarioShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf contains DEFAULT_CPF
        defaultUsuarioShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf contains UPDATED_CPF
        defaultUsuarioShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where cpf does not contain DEFAULT_CPF
        defaultUsuarioShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the usuarioList where cpf does not contain UPDATED_CPF
        defaultUsuarioShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email equals to DEFAULT_EMAIL
        defaultUsuarioShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email equals to UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email not equals to DEFAULT_EMAIL
        defaultUsuarioShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email not equals to UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the usuarioList where email equals to UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email is not null
        defaultUsuarioShouldBeFound("email.specified=true");

        // Get all the usuarioList where email is null
        defaultUsuarioShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email contains DEFAULT_EMAIL
        defaultUsuarioShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email contains UPDATED_EMAIL
        defaultUsuarioShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email does not contain DEFAULT_EMAIL
        defaultUsuarioShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the usuarioList where email does not contain UPDATED_EMAIL
        defaultUsuarioShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular equals to DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular not equals to DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular not equals to UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular is not null
        defaultUsuarioShouldBeFound("celular.specified=true");

        // Get all the usuarioList where celular is null
        defaultUsuarioShouldNotBeFound("celular.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular contains DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular contains UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular does not contain DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular does not contain UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    void getAllUsuariosByWhatsappIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where whatsapp equals to DEFAULT_WHATSAPP
        defaultUsuarioShouldBeFound("whatsapp.equals=" + DEFAULT_WHATSAPP);

        // Get all the usuarioList where whatsapp equals to UPDATED_WHATSAPP
        defaultUsuarioShouldNotBeFound("whatsapp.equals=" + UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void getAllUsuariosByWhatsappIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where whatsapp not equals to DEFAULT_WHATSAPP
        defaultUsuarioShouldNotBeFound("whatsapp.notEquals=" + DEFAULT_WHATSAPP);

        // Get all the usuarioList where whatsapp not equals to UPDATED_WHATSAPP
        defaultUsuarioShouldBeFound("whatsapp.notEquals=" + UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void getAllUsuariosByWhatsappIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where whatsapp in DEFAULT_WHATSAPP or UPDATED_WHATSAPP
        defaultUsuarioShouldBeFound("whatsapp.in=" + DEFAULT_WHATSAPP + "," + UPDATED_WHATSAPP);

        // Get all the usuarioList where whatsapp equals to UPDATED_WHATSAPP
        defaultUsuarioShouldNotBeFound("whatsapp.in=" + UPDATED_WHATSAPP);
    }

    @Test
    @Transactional
    void getAllUsuariosByWhatsappIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where whatsapp is not null
        defaultUsuarioShouldBeFound("whatsapp.specified=true");

        // Get all the usuarioList where whatsapp is null
        defaultUsuarioShouldNotBeFound("whatsapp.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where ativo equals to DEFAULT_ATIVO
        defaultUsuarioShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the usuarioList where ativo equals to UPDATED_ATIVO
        defaultUsuarioShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where ativo not equals to DEFAULT_ATIVO
        defaultUsuarioShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the usuarioList where ativo not equals to UPDATED_ATIVO
        defaultUsuarioShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultUsuarioShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the usuarioList where ativo equals to UPDATED_ATIVO
        defaultUsuarioShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllUsuariosByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where ativo is not null
        defaultUsuarioShouldBeFound("ativo.specified=true");

        // Get all the usuarioList where ativo is null
        defaultUsuarioShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado equals to DEFAULT_CRIADO
        defaultUsuarioShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado equals to UPDATED_CRIADO
        defaultUsuarioShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado not equals to DEFAULT_CRIADO
        defaultUsuarioShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado not equals to UPDATED_CRIADO
        defaultUsuarioShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultUsuarioShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the usuarioList where criado equals to UPDATED_CRIADO
        defaultUsuarioShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado is not null
        defaultUsuarioShouldBeFound("criado.specified=true");

        // Get all the usuarioList where criado is null
        defaultUsuarioShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado is greater than or equal to DEFAULT_CRIADO
        defaultUsuarioShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado is greater than or equal to UPDATED_CRIADO
        defaultUsuarioShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado is less than or equal to DEFAULT_CRIADO
        defaultUsuarioShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado is less than or equal to SMALLER_CRIADO
        defaultUsuarioShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado is less than DEFAULT_CRIADO
        defaultUsuarioShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado is less than UPDATED_CRIADO
        defaultUsuarioShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where criado is greater than DEFAULT_CRIADO
        defaultUsuarioShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the usuarioList where criado is greater than SMALLER_CRIADO
        defaultUsuarioShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllUsuariosByNivelCNHIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nivelCNH equals to DEFAULT_NIVEL_CNH
        defaultUsuarioShouldBeFound("nivelCNH.equals=" + DEFAULT_NIVEL_CNH);

        // Get all the usuarioList where nivelCNH equals to UPDATED_NIVEL_CNH
        defaultUsuarioShouldNotBeFound("nivelCNH.equals=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllUsuariosByNivelCNHIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nivelCNH not equals to DEFAULT_NIVEL_CNH
        defaultUsuarioShouldNotBeFound("nivelCNH.notEquals=" + DEFAULT_NIVEL_CNH);

        // Get all the usuarioList where nivelCNH not equals to UPDATED_NIVEL_CNH
        defaultUsuarioShouldBeFound("nivelCNH.notEquals=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllUsuariosByNivelCNHIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nivelCNH in DEFAULT_NIVEL_CNH or UPDATED_NIVEL_CNH
        defaultUsuarioShouldBeFound("nivelCNH.in=" + DEFAULT_NIVEL_CNH + "," + UPDATED_NIVEL_CNH);

        // Get all the usuarioList where nivelCNH equals to UPDATED_NIVEL_CNH
        defaultUsuarioShouldNotBeFound("nivelCNH.in=" + UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void getAllUsuariosByNivelCNHIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nivelCNH is not null
        defaultUsuarioShouldBeFound("nivelCNH.specified=true");

        // Get all the usuarioList where nivelCNH is null
        defaultUsuarioShouldNotBeFound("nivelCNH.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMinhasSolicitacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Solicitacao minhasSolicitacoes = SolicitacaoResourceIT.createEntity(em);
        em.persist(minhasSolicitacoes);
        em.flush();
        usuario.addMinhasSolicitacoes(minhasSolicitacoes);
        usuarioRepository.saveAndFlush(usuario);
        Long minhasSolicitacoesId = minhasSolicitacoes.getId();

        // Get all the usuarioList where minhasSolicitacoes equals to minhasSolicitacoesId
        defaultUsuarioShouldBeFound("minhasSolicitacoesId.equals=" + minhasSolicitacoesId);

        // Get all the usuarioList where minhasSolicitacoes equals to (minhasSolicitacoesId + 1)
        defaultUsuarioShouldNotBeFound("minhasSolicitacoesId.equals=" + (minhasSolicitacoesId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByComoAutorizadorIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Solicitacao comoAutorizador = SolicitacaoResourceIT.createEntity(em);
        em.persist(comoAutorizador);
        em.flush();
        usuario.addComoAutorizador(comoAutorizador);
        usuarioRepository.saveAndFlush(usuario);
        Long comoAutorizadorId = comoAutorizador.getId();

        // Get all the usuarioList where comoAutorizador equals to comoAutorizadorId
        defaultUsuarioShouldBeFound("comoAutorizadorId.equals=" + comoAutorizadorId);

        // Get all the usuarioList where comoAutorizador equals to (comoAutorizadorId + 1)
        defaultUsuarioShouldNotBeFound("comoAutorizadorId.equals=" + (comoAutorizadorId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByComoMotoristaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Solicitacao comoMotorista = SolicitacaoResourceIT.createEntity(em);
        em.persist(comoMotorista);
        em.flush();
        usuario.addComoMotorista(comoMotorista);
        usuarioRepository.saveAndFlush(usuario);
        Long comoMotoristaId = comoMotorista.getId();

        // Get all the usuarioList where comoMotorista equals to comoMotoristaId
        defaultUsuarioShouldBeFound("comoMotoristaId.equals=" + comoMotoristaId);

        // Get all the usuarioList where comoMotorista equals to (comoMotoristaId + 1)
        defaultUsuarioShouldNotBeFound("comoMotoristaId.equals=" + (comoMotoristaId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByVeiculosHabilitadosIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Veiculo veiculosHabilitados = VeiculoResourceIT.createEntity(em);
        em.persist(veiculosHabilitados);
        em.flush();
        usuario.addVeiculosHabilitados(veiculosHabilitados);
        usuarioRepository.saveAndFlush(usuario);
        Long veiculosHabilitadosId = veiculosHabilitados.getId();

        // Get all the usuarioList where veiculosHabilitados equals to veiculosHabilitadosId
        defaultUsuarioShouldBeFound("veiculosHabilitadosId.equals=" + veiculosHabilitadosId);

        // Get all the usuarioList where veiculosHabilitados equals to (veiculosHabilitadosId + 1)
        defaultUsuarioShouldNotBeFound("veiculosHabilitadosId.equals=" + (veiculosHabilitadosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].perfil").value(hasItem(DEFAULT_PERFIL.toString())))
            .andExpect(jsonPath("$.[*].identificacao").value(hasItem(DEFAULT_IDENTIFICACAO.toString())))
            .andExpect(jsonPath("$.[*].nidentificao").value(hasItem(DEFAULT_NIDENTIFICAO)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].whatsapp").value(hasItem(DEFAULT_WHATSAPP.booleanValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].nivelCNH").value(hasItem(DEFAULT_NIVEL_CNH.toString())));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .nome(UPDATED_NOME)
            .perfil(UPDATED_PERFIL)
            .identificacao(UPDATED_IDENTIFICACAO)
            .nidentificao(UPDATED_NIDENTIFICAO)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .whatsapp(UPDATED_WHATSAPP)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO)
            .nivelCNH(UPDATED_NIVEL_CNH);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getPerfil()).isEqualTo(UPDATED_PERFIL);
        assertThat(testUsuario.getIdentificacao()).isEqualTo(UPDATED_IDENTIFICACAO);
        assertThat(testUsuario.getNidentificao()).isEqualTo(UPDATED_NIDENTIFICAO);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
        assertThat(testUsuario.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testUsuario.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testUsuario.getNivelCNH()).isEqualTo(UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nome(UPDATED_NOME)
            .identificacao(UPDATED_IDENTIFICACAO)
            .nidentificao(UPDATED_NIDENTIFICAO)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .criado(UPDATED_CRIADO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getPerfil()).isEqualTo(DEFAULT_PERFIL);
        assertThat(testUsuario.getIdentificacao()).isEqualTo(UPDATED_IDENTIFICACAO);
        assertThat(testUsuario.getNidentificao()).isEqualTo(UPDATED_NIDENTIFICAO);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getWhatsapp()).isEqualTo(DEFAULT_WHATSAPP);
        assertThat(testUsuario.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testUsuario.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testUsuario.getNivelCNH()).isEqualTo(DEFAULT_NIVEL_CNH);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nome(UPDATED_NOME)
            .perfil(UPDATED_PERFIL)
            .identificacao(UPDATED_IDENTIFICACAO)
            .nidentificao(UPDATED_NIDENTIFICAO)
            .cpf(UPDATED_CPF)
            .email(UPDATED_EMAIL)
            .celular(UPDATED_CELULAR)
            .whatsapp(UPDATED_WHATSAPP)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO)
            .nivelCNH(UPDATED_NIVEL_CNH);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getPerfil()).isEqualTo(UPDATED_PERFIL);
        assertThat(testUsuario.getIdentificacao()).isEqualTo(UPDATED_IDENTIFICACAO);
        assertThat(testUsuario.getNidentificao()).isEqualTo(UPDATED_NIDENTIFICAO);
        assertThat(testUsuario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getWhatsapp()).isEqualTo(UPDATED_WHATSAPP);
        assertThat(testUsuario.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testUsuario.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testUsuario.getNivelCNH()).isEqualTo(UPDATED_NIVEL_CNH);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
