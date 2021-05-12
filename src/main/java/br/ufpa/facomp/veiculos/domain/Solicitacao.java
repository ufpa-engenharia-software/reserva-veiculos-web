package br.ufpa.facomp.veiculos.domain;

import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Solicitacao.
 */
@Entity
@Table(name = "solicitacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Solicitacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "origem")
    private String origem;

    @Column(name = "destino")
    private String destino;

    @Column(name = "data_solicitacao")
    private ZonedDateTime dataSolicitacao;

    @Column(name = "horario_saida")
    private ZonedDateTime horarioSaida;

    @Column(name = "horario_retorno")
    private ZonedDateTime horarioRetorno;

    @Column(name = "distancia_estimada_km")
    private Double distanciaEstimadaKm;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "justificativa")
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "n_pessoas")
    private Integer nPessoas;

    @Column(name = "peso")
    private Double peso;

    @ManyToOne
    private CategoriaVeiculo categoria;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fabricante", "categoria", "motoristasHabilitados" }, allowSetters = true)
    private Veiculo veiculoAlocado;

    @JsonIgnoreProperties(value = { "solicitacao" }, allowSetters = true)
    @OneToOne(mappedBy = "solicitacao")
    private AvaliacaoSolicitacao avaliacao;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "minhasSolicitacoes", "comoAutorizadors", "comoMotoristas", "veiculosHabilitados" },
        allowSetters = true
    )
    private Usuario solicitante;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "minhasSolicitacoes", "comoAutorizadors", "comoMotoristas", "veiculosHabilitados" },
        allowSetters = true
    )
    private Usuario autorizador;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "minhasSolicitacoes", "comoAutorizadors", "comoMotoristas", "veiculosHabilitados" },
        allowSetters = true
    )
    private Usuario motorista;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solicitacao id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrigem() {
        return this.origem;
    }

    public Solicitacao origem(String origem) {
        this.origem = origem;
        return this;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return this.destino;
    }

    public Solicitacao destino(String destino) {
        this.destino = destino;
        return this;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public ZonedDateTime getDataSolicitacao() {
        return this.dataSolicitacao;
    }

    public Solicitacao dataSolicitacao(ZonedDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
        return this;
    }

    public void setDataSolicitacao(ZonedDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public ZonedDateTime getHorarioSaida() {
        return this.horarioSaida;
    }

    public Solicitacao horarioSaida(ZonedDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
        return this;
    }

    public void setHorarioSaida(ZonedDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public ZonedDateTime getHorarioRetorno() {
        return this.horarioRetorno;
    }

    public Solicitacao horarioRetorno(ZonedDateTime horarioRetorno) {
        this.horarioRetorno = horarioRetorno;
        return this;
    }

    public void setHorarioRetorno(ZonedDateTime horarioRetorno) {
        this.horarioRetorno = horarioRetorno;
    }

    public Double getDistanciaEstimadaKm() {
        return this.distanciaEstimadaKm;
    }

    public Solicitacao distanciaEstimadaKm(Double distanciaEstimadaKm) {
        this.distanciaEstimadaKm = distanciaEstimadaKm;
        return this;
    }

    public void setDistanciaEstimadaKm(Double distanciaEstimadaKm) {
        this.distanciaEstimadaKm = distanciaEstimadaKm;
    }

    public String getJustificativa() {
        return this.justificativa;
    }

    public Solicitacao justificativa(String justificativa) {
        this.justificativa = justificativa;
        return this;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Status getStatus() {
        return this.status;
    }

    public Solicitacao status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getnPessoas() {
        return this.nPessoas;
    }

    public Solicitacao nPessoas(Integer nPessoas) {
        this.nPessoas = nPessoas;
        return this;
    }

    public void setnPessoas(Integer nPessoas) {
        this.nPessoas = nPessoas;
    }

    public Double getPeso() {
        return this.peso;
    }

    public Solicitacao peso(Double peso) {
        this.peso = peso;
        return this;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public CategoriaVeiculo getCategoria() {
        return this.categoria;
    }

    public Solicitacao categoria(CategoriaVeiculo categoriaVeiculo) {
        this.setCategoria(categoriaVeiculo);
        return this;
    }

    public void setCategoria(CategoriaVeiculo categoriaVeiculo) {
        this.categoria = categoriaVeiculo;
    }

    public Veiculo getVeiculoAlocado() {
        return this.veiculoAlocado;
    }

    public Solicitacao veiculoAlocado(Veiculo veiculo) {
        this.setVeiculoAlocado(veiculo);
        return this;
    }

    public void setVeiculoAlocado(Veiculo veiculo) {
        this.veiculoAlocado = veiculo;
    }

    public AvaliacaoSolicitacao getAvaliacao() {
        return this.avaliacao;
    }

    public Solicitacao avaliacao(AvaliacaoSolicitacao avaliacaoSolicitacao) {
        this.setAvaliacao(avaliacaoSolicitacao);
        return this;
    }

    public void setAvaliacao(AvaliacaoSolicitacao avaliacaoSolicitacao) {
        if (this.avaliacao != null) {
            this.avaliacao.setSolicitacao(null);
        }
        if (avaliacao != null) {
            avaliacao.setSolicitacao(this);
        }
        this.avaliacao = avaliacaoSolicitacao;
    }

    public Usuario getSolicitante() {
        return this.solicitante;
    }

    public Solicitacao solicitante(Usuario usuario) {
        this.setSolicitante(usuario);
        return this;
    }

    public void setSolicitante(Usuario usuario) {
        this.solicitante = usuario;
    }

    public Usuario getAutorizador() {
        return this.autorizador;
    }

    public Solicitacao autorizador(Usuario usuario) {
        this.setAutorizador(usuario);
        return this;
    }

    public void setAutorizador(Usuario usuario) {
        this.autorizador = usuario;
    }

    public Usuario getMotorista() {
        return this.motorista;
    }

    public Solicitacao motorista(Usuario usuario) {
        this.setMotorista(usuario);
        return this;
    }

    public void setMotorista(Usuario usuario) {
        this.motorista = usuario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Solicitacao)) {
            return false;
        }
        return id != null && id.equals(((Solicitacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Solicitacao{" +
            "id=" + getId() +
            ", origem='" + getOrigem() + "'" +
            ", destino='" + getDestino() + "'" +
            ", dataSolicitacao='" + getDataSolicitacao() + "'" +
            ", horarioSaida='" + getHorarioSaida() + "'" +
            ", horarioRetorno='" + getHorarioRetorno() + "'" +
            ", distanciaEstimadaKm=" + getDistanciaEstimadaKm() +
            ", justificativa='" + getJustificativa() + "'" +
            ", status='" + getStatus() + "'" +
            ", nPessoas=" + getnPessoas() +
            ", peso=" + getPeso() +
            "}";
    }
}
