package br.ufpa.facomp.veiculos.service.dto;

import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao} entity.
 */
public class AvaliacaoSolicitacaoDTO implements Serializable {

    private Long id;

    private Double valorGasolina;

    private Double totalGasto;

    private Status statusSolicitacao;

    @Lob
    private String justificativaStatus;

    private SolicitacaoDTO solicitacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorGasolina() {
        return valorGasolina;
    }

    public void setValorGasolina(Double valorGasolina) {
        this.valorGasolina = valorGasolina;
    }

    public Double getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(Double totalGasto) {
        this.totalGasto = totalGasto;
    }

    public Status getStatusSolicitacao() {
        return statusSolicitacao;
    }

    public void setStatusSolicitacao(Status statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }

    public String getJustificativaStatus() {
        return justificativaStatus;
    }

    public void setJustificativaStatus(String justificativaStatus) {
        this.justificativaStatus = justificativaStatus;
    }

    public SolicitacaoDTO getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SolicitacaoDTO solicitacao) {
        this.solicitacao = solicitacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvaliacaoSolicitacaoDTO)) {
            return false;
        }

        AvaliacaoSolicitacaoDTO avaliacaoSolicitacaoDTO = (AvaliacaoSolicitacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, avaliacaoSolicitacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvaliacaoSolicitacaoDTO{" +
            "id=" + getId() +
            ", valorGasolina=" + getValorGasolina() +
            ", totalGasto=" + getTotalGasto() +
            ", statusSolicitacao='" + getStatusSolicitacao() + "'" +
            ", justificativaStatus='" + getJustificativaStatus() + "'" +
            ", solicitacao=" + getSolicitacao() +
            "}";
    }
}
