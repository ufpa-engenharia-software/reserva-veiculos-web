package br.ufpa.facomp.veiculos.domain;

import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AvaliacaoSolicitacao.
 */
@Entity
@Table(name = "avaliacao_solicitacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AvaliacaoSolicitacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "valor_gasolina")
    private Double valorGasolina;

    @Column(name = "total_gasto")
    private Double totalGasto;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_solicitacao")
    private Status statusSolicitacao;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "justificativa_status")
    private String justificativaStatus;

    @JsonIgnoreProperties(
        value = { "categoria", "veiculoAlocado", "avaliacao", "solicitante", "autorizador", "motorista" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Solicitacao solicitacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AvaliacaoSolicitacao id(Long id) {
        this.id = id;
        return this;
    }

    public Double getValorGasolina() {
        return this.valorGasolina;
    }

    public AvaliacaoSolicitacao valorGasolina(Double valorGasolina) {
        this.valorGasolina = valorGasolina;
        return this;
    }

    public void setValorGasolina(Double valorGasolina) {
        this.valorGasolina = valorGasolina;
    }

    public Double getTotalGasto() {
        return this.totalGasto;
    }

    public AvaliacaoSolicitacao totalGasto(Double totalGasto) {
        this.totalGasto = totalGasto;
        return this;
    }

    public void setTotalGasto(Double totalGasto) {
        this.totalGasto = totalGasto;
    }

    public Status getStatusSolicitacao() {
        return this.statusSolicitacao;
    }

    public AvaliacaoSolicitacao statusSolicitacao(Status statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
        return this;
    }

    public void setStatusSolicitacao(Status statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }

    public String getJustificativaStatus() {
        return this.justificativaStatus;
    }

    public AvaliacaoSolicitacao justificativaStatus(String justificativaStatus) {
        this.justificativaStatus = justificativaStatus;
        return this;
    }

    public void setJustificativaStatus(String justificativaStatus) {
        this.justificativaStatus = justificativaStatus;
    }

    public Solicitacao getSolicitacao() {
        return this.solicitacao;
    }

    public AvaliacaoSolicitacao solicitacao(Solicitacao solicitacao) {
        this.setSolicitacao(solicitacao);
        return this;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvaliacaoSolicitacao)) {
            return false;
        }
        return id != null && id.equals(((AvaliacaoSolicitacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvaliacaoSolicitacao{" +
            "id=" + getId() +
            ", valorGasolina=" + getValorGasolina() +
            ", totalGasto=" + getTotalGasto() +
            ", statusSolicitacao='" + getStatusSolicitacao() + "'" +
            ", justificativaStatus='" + getJustificativaStatus() + "'" +
            "}";
    }
}
