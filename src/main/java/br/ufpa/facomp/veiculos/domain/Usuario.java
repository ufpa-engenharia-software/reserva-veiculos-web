package br.ufpa.facomp.veiculos.domain;

import br.ufpa.facomp.veiculos.domain.enumeration.Identificador;
import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import br.ufpa.facomp.veiculos.domain.enumeration.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private PerfilUsuario perfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "identificacao")
    private Identificador identificacao;

    @Column(name = "nidentificao")
    private String nidentificao;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "celular")
    private String celular;

    @Column(name = "whatsapp")
    private Boolean whatsapp;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_cnh")
    private NivelCNH nivelCNH;

    @OneToMany(mappedBy = "solicitante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "categoria", "veiculoAlocado", "avaliacao", "solicitante", "autorizador", "motorista" },
        allowSetters = true
    )
    private Set<Solicitacao> minhasSolicitacoes = new HashSet<>();

    @OneToMany(mappedBy = "autorizador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "categoria", "veiculoAlocado", "avaliacao", "solicitante", "autorizador", "motorista" },
        allowSetters = true
    )
    private Set<Solicitacao> comoAutorizadors = new HashSet<>();

    @OneToMany(mappedBy = "motorista")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "categoria", "veiculoAlocado", "avaliacao", "solicitante", "autorizador", "motorista" },
        allowSetters = true
    )
    private Set<Solicitacao> comoMotoristas = new HashSet<>();

    @ManyToMany(mappedBy = "motoristasHabilitados")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fabricante", "categoria", "motoristasHabilitados" }, allowSetters = true)
    private Set<Veiculo> veiculosHabilitados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Usuario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PerfilUsuario getPerfil() {
        return this.perfil;
    }

    public Usuario perfil(PerfilUsuario perfil) {
        this.perfil = perfil;
        return this;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public Identificador getIdentificacao() {
        return this.identificacao;
    }

    public Usuario identificacao(Identificador identificacao) {
        this.identificacao = identificacao;
        return this;
    }

    public void setIdentificacao(Identificador identificacao) {
        this.identificacao = identificacao;
    }

    public String getNidentificao() {
        return this.nidentificao;
    }

    public Usuario nidentificao(String nidentificao) {
        this.nidentificao = nidentificao;
        return this;
    }

    public void setNidentificao(String nidentificao) {
        this.nidentificao = nidentificao;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Usuario cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return this.celular;
    }

    public Usuario celular(String celular) {
        this.celular = celular;
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Boolean getWhatsapp() {
        return this.whatsapp;
    }

    public Usuario whatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
        return this;
    }

    public void setWhatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Usuario ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Usuario criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public NivelCNH getNivelCNH() {
        return this.nivelCNH;
    }

    public Usuario nivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
        return this;
    }

    public void setNivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
    }

    public Set<Solicitacao> getMinhasSolicitacoes() {
        return this.minhasSolicitacoes;
    }

    public Usuario minhasSolicitacoes(Set<Solicitacao> solicitacaos) {
        this.setMinhasSolicitacoes(solicitacaos);
        return this;
    }

    public Usuario addMinhasSolicitacoes(Solicitacao solicitacao) {
        this.minhasSolicitacoes.add(solicitacao);
        solicitacao.setSolicitante(this);
        return this;
    }

    public Usuario removeMinhasSolicitacoes(Solicitacao solicitacao) {
        this.minhasSolicitacoes.remove(solicitacao);
        solicitacao.setSolicitante(null);
        return this;
    }

    public void setMinhasSolicitacoes(Set<Solicitacao> solicitacaos) {
        if (this.minhasSolicitacoes != null) {
            this.minhasSolicitacoes.forEach(i -> i.setSolicitante(null));
        }
        if (solicitacaos != null) {
            solicitacaos.forEach(i -> i.setSolicitante(this));
        }
        this.minhasSolicitacoes = solicitacaos;
    }

    public Set<Solicitacao> getComoAutorizadors() {
        return this.comoAutorizadors;
    }

    public Usuario comoAutorizadors(Set<Solicitacao> solicitacaos) {
        this.setComoAutorizadors(solicitacaos);
        return this;
    }

    public Usuario addComoAutorizador(Solicitacao solicitacao) {
        this.comoAutorizadors.add(solicitacao);
        solicitacao.setAutorizador(this);
        return this;
    }

    public Usuario removeComoAutorizador(Solicitacao solicitacao) {
        this.comoAutorizadors.remove(solicitacao);
        solicitacao.setAutorizador(null);
        return this;
    }

    public void setComoAutorizadors(Set<Solicitacao> solicitacaos) {
        if (this.comoAutorizadors != null) {
            this.comoAutorizadors.forEach(i -> i.setAutorizador(null));
        }
        if (solicitacaos != null) {
            solicitacaos.forEach(i -> i.setAutorizador(this));
        }
        this.comoAutorizadors = solicitacaos;
    }

    public Set<Solicitacao> getComoMotoristas() {
        return this.comoMotoristas;
    }

    public Usuario comoMotoristas(Set<Solicitacao> solicitacaos) {
        this.setComoMotoristas(solicitacaos);
        return this;
    }

    public Usuario addComoMotorista(Solicitacao solicitacao) {
        this.comoMotoristas.add(solicitacao);
        solicitacao.setMotorista(this);
        return this;
    }

    public Usuario removeComoMotorista(Solicitacao solicitacao) {
        this.comoMotoristas.remove(solicitacao);
        solicitacao.setMotorista(null);
        return this;
    }

    public void setComoMotoristas(Set<Solicitacao> solicitacaos) {
        if (this.comoMotoristas != null) {
            this.comoMotoristas.forEach(i -> i.setMotorista(null));
        }
        if (solicitacaos != null) {
            solicitacaos.forEach(i -> i.setMotorista(this));
        }
        this.comoMotoristas = solicitacaos;
    }

    public Set<Veiculo> getVeiculosHabilitados() {
        return this.veiculosHabilitados;
    }

    public Usuario veiculosHabilitados(Set<Veiculo> veiculos) {
        this.setVeiculosHabilitados(veiculos);
        return this;
    }

    public Usuario addVeiculosHabilitados(Veiculo veiculo) {
        this.veiculosHabilitados.add(veiculo);
        veiculo.getMotoristasHabilitados().add(this);
        return this;
    }

    public Usuario removeVeiculosHabilitados(Veiculo veiculo) {
        this.veiculosHabilitados.remove(veiculo);
        veiculo.getMotoristasHabilitados().remove(this);
        return this;
    }

    public void setVeiculosHabilitados(Set<Veiculo> veiculos) {
        if (this.veiculosHabilitados != null) {
            this.veiculosHabilitados.forEach(i -> i.removeMotoristasHabilitados(this));
        }
        if (veiculos != null) {
            veiculos.forEach(i -> i.addMotoristasHabilitados(this));
        }
        this.veiculosHabilitados = veiculos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", identificacao='" + getIdentificacao() + "'" +
            ", nidentificao='" + getNidentificao() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", email='" + getEmail() + "'" +
            ", celular='" + getCelular() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", criado='" + getCriado() + "'" +
            ", nivelCNH='" + getNivelCNH() + "'" +
            "}";
    }
}
