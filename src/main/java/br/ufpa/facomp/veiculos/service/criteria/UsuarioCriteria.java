package br.ufpa.facomp.veiculos.service.criteria;

import br.ufpa.facomp.veiculos.domain.enumeration.Identificador;
import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import br.ufpa.facomp.veiculos.domain.enumeration.PerfilUsuario;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.Usuario} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PerfilUsuario
     */
    public static class PerfilUsuarioFilter extends Filter<PerfilUsuario> {

        public PerfilUsuarioFilter() {}

        public PerfilUsuarioFilter(PerfilUsuarioFilter filter) {
            super(filter);
        }

        @Override
        public PerfilUsuarioFilter copy() {
            return new PerfilUsuarioFilter(this);
        }
    }

    /**
     * Class for filtering Identificador
     */
    public static class IdentificadorFilter extends Filter<Identificador> {

        public IdentificadorFilter() {}

        public IdentificadorFilter(IdentificadorFilter filter) {
            super(filter);
        }

        @Override
        public IdentificadorFilter copy() {
            return new IdentificadorFilter(this);
        }
    }

    /**
     * Class for filtering NivelCNH
     */
    public static class NivelCNHFilter extends Filter<NivelCNH> {

        public NivelCNHFilter() {}

        public NivelCNHFilter(NivelCNHFilter filter) {
            super(filter);
        }

        @Override
        public NivelCNHFilter copy() {
            return new NivelCNHFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private PerfilUsuarioFilter perfil;

    private IdentificadorFilter identificacao;

    private StringFilter nidentificao;

    private StringFilter cpf;

    private StringFilter email;

    private StringFilter celular;

    private BooleanFilter whatsapp;

    private BooleanFilter ativo;

    private ZonedDateTimeFilter criado;

    private NivelCNHFilter nivelCNH;

    private LongFilter minhasSolicitacoesId;

    private LongFilter comoAutorizadorId;

    private LongFilter comoMotoristaId;

    private LongFilter veiculosHabilitadosId;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.perfil = other.perfil == null ? null : other.perfil.copy();
        this.identificacao = other.identificacao == null ? null : other.identificacao.copy();
        this.nidentificao = other.nidentificao == null ? null : other.nidentificao.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.celular = other.celular == null ? null : other.celular.copy();
        this.whatsapp = other.whatsapp == null ? null : other.whatsapp.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.nivelCNH = other.nivelCNH == null ? null : other.nivelCNH.copy();
        this.minhasSolicitacoesId = other.minhasSolicitacoesId == null ? null : other.minhasSolicitacoesId.copy();
        this.comoAutorizadorId = other.comoAutorizadorId == null ? null : other.comoAutorizadorId.copy();
        this.comoMotoristaId = other.comoMotoristaId == null ? null : other.comoMotoristaId.copy();
        this.veiculosHabilitadosId = other.veiculosHabilitadosId == null ? null : other.veiculosHabilitadosId.copy();
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public PerfilUsuarioFilter getPerfil() {
        return perfil;
    }

    public PerfilUsuarioFilter perfil() {
        if (perfil == null) {
            perfil = new PerfilUsuarioFilter();
        }
        return perfil;
    }

    public void setPerfil(PerfilUsuarioFilter perfil) {
        this.perfil = perfil;
    }

    public IdentificadorFilter getIdentificacao() {
        return identificacao;
    }

    public IdentificadorFilter identificacao() {
        if (identificacao == null) {
            identificacao = new IdentificadorFilter();
        }
        return identificacao;
    }

    public void setIdentificacao(IdentificadorFilter identificacao) {
        this.identificacao = identificacao;
    }

    public StringFilter getNidentificao() {
        return nidentificao;
    }

    public StringFilter nidentificao() {
        if (nidentificao == null) {
            nidentificao = new StringFilter();
        }
        return nidentificao;
    }

    public void setNidentificao(StringFilter nidentificao) {
        this.nidentificao = nidentificao;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getCelular() {
        return celular;
    }

    public StringFilter celular() {
        if (celular == null) {
            celular = new StringFilter();
        }
        return celular;
    }

    public void setCelular(StringFilter celular) {
        this.celular = celular;
    }

    public BooleanFilter getWhatsapp() {
        return whatsapp;
    }

    public BooleanFilter whatsapp() {
        if (whatsapp == null) {
            whatsapp = new BooleanFilter();
        }
        return whatsapp;
    }

    public void setWhatsapp(BooleanFilter whatsapp) {
        this.whatsapp = whatsapp;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            ativo = new BooleanFilter();
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTimeFilter getCriado() {
        return criado;
    }

    public ZonedDateTimeFilter criado() {
        if (criado == null) {
            criado = new ZonedDateTimeFilter();
        }
        return criado;
    }

    public void setCriado(ZonedDateTimeFilter criado) {
        this.criado = criado;
    }

    public NivelCNHFilter getNivelCNH() {
        return nivelCNH;
    }

    public NivelCNHFilter nivelCNH() {
        if (nivelCNH == null) {
            nivelCNH = new NivelCNHFilter();
        }
        return nivelCNH;
    }

    public void setNivelCNH(NivelCNHFilter nivelCNH) {
        this.nivelCNH = nivelCNH;
    }

    public LongFilter getMinhasSolicitacoesId() {
        return minhasSolicitacoesId;
    }

    public LongFilter minhasSolicitacoesId() {
        if (minhasSolicitacoesId == null) {
            minhasSolicitacoesId = new LongFilter();
        }
        return minhasSolicitacoesId;
    }

    public void setMinhasSolicitacoesId(LongFilter minhasSolicitacoesId) {
        this.minhasSolicitacoesId = minhasSolicitacoesId;
    }

    public LongFilter getComoAutorizadorId() {
        return comoAutorizadorId;
    }

    public LongFilter comoAutorizadorId() {
        if (comoAutorizadorId == null) {
            comoAutorizadorId = new LongFilter();
        }
        return comoAutorizadorId;
    }

    public void setComoAutorizadorId(LongFilter comoAutorizadorId) {
        this.comoAutorizadorId = comoAutorizadorId;
    }

    public LongFilter getComoMotoristaId() {
        return comoMotoristaId;
    }

    public LongFilter comoMotoristaId() {
        if (comoMotoristaId == null) {
            comoMotoristaId = new LongFilter();
        }
        return comoMotoristaId;
    }

    public void setComoMotoristaId(LongFilter comoMotoristaId) {
        this.comoMotoristaId = comoMotoristaId;
    }

    public LongFilter getVeiculosHabilitadosId() {
        return veiculosHabilitadosId;
    }

    public LongFilter veiculosHabilitadosId() {
        if (veiculosHabilitadosId == null) {
            veiculosHabilitadosId = new LongFilter();
        }
        return veiculosHabilitadosId;
    }

    public void setVeiculosHabilitadosId(LongFilter veiculosHabilitadosId) {
        this.veiculosHabilitadosId = veiculosHabilitadosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(perfil, that.perfil) &&
            Objects.equals(identificacao, that.identificacao) &&
            Objects.equals(nidentificao, that.nidentificao) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(email, that.email) &&
            Objects.equals(celular, that.celular) &&
            Objects.equals(whatsapp, that.whatsapp) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(nivelCNH, that.nivelCNH) &&
            Objects.equals(minhasSolicitacoesId, that.minhasSolicitacoesId) &&
            Objects.equals(comoAutorizadorId, that.comoAutorizadorId) &&
            Objects.equals(comoMotoristaId, that.comoMotoristaId) &&
            Objects.equals(veiculosHabilitadosId, that.veiculosHabilitadosId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            perfil,
            identificacao,
            nidentificao,
            cpf,
            email,
            celular,
            whatsapp,
            ativo,
            criado,
            nivelCNH,
            minhasSolicitacoesId,
            comoAutorizadorId,
            comoMotoristaId,
            veiculosHabilitadosId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (perfil != null ? "perfil=" + perfil + ", " : "") +
            (identificacao != null ? "identificacao=" + identificacao + ", " : "") +
            (nidentificao != null ? "nidentificao=" + nidentificao + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (celular != null ? "celular=" + celular + ", " : "") +
            (whatsapp != null ? "whatsapp=" + whatsapp + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (nivelCNH != null ? "nivelCNH=" + nivelCNH + ", " : "") +
            (minhasSolicitacoesId != null ? "minhasSolicitacoesId=" + minhasSolicitacoesId + ", " : "") +
            (comoAutorizadorId != null ? "comoAutorizadorId=" + comoAutorizadorId + ", " : "") +
            (comoMotoristaId != null ? "comoMotoristaId=" + comoMotoristaId + ", " : "") +
            (veiculosHabilitadosId != null ? "veiculosHabilitadosId=" + veiculosHabilitadosId + ", " : "") +
            "}";
    }
}
