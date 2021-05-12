package br.ufpa.facomp.veiculos.service.criteria;

import br.ufpa.facomp.veiculos.domain.enumeration.Status;
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

/**
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.AvaliacaoSolicitacao} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.AvaliacaoSolicitacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /avaliacao-solicitacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AvaliacaoSolicitacaoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter valorGasolina;

    private DoubleFilter totalGasto;

    private StatusFilter statusSolicitacao;

    private LongFilter solicitacaoId;

    public AvaliacaoSolicitacaoCriteria() {}

    public AvaliacaoSolicitacaoCriteria(AvaliacaoSolicitacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valorGasolina = other.valorGasolina == null ? null : other.valorGasolina.copy();
        this.totalGasto = other.totalGasto == null ? null : other.totalGasto.copy();
        this.statusSolicitacao = other.statusSolicitacao == null ? null : other.statusSolicitacao.copy();
        this.solicitacaoId = other.solicitacaoId == null ? null : other.solicitacaoId.copy();
    }

    @Override
    public AvaliacaoSolicitacaoCriteria copy() {
        return new AvaliacaoSolicitacaoCriteria(this);
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

    public DoubleFilter getValorGasolina() {
        return valorGasolina;
    }

    public DoubleFilter valorGasolina() {
        if (valorGasolina == null) {
            valorGasolina = new DoubleFilter();
        }
        return valorGasolina;
    }

    public void setValorGasolina(DoubleFilter valorGasolina) {
        this.valorGasolina = valorGasolina;
    }

    public DoubleFilter getTotalGasto() {
        return totalGasto;
    }

    public DoubleFilter totalGasto() {
        if (totalGasto == null) {
            totalGasto = new DoubleFilter();
        }
        return totalGasto;
    }

    public void setTotalGasto(DoubleFilter totalGasto) {
        this.totalGasto = totalGasto;
    }

    public StatusFilter getStatusSolicitacao() {
        return statusSolicitacao;
    }

    public StatusFilter statusSolicitacao() {
        if (statusSolicitacao == null) {
            statusSolicitacao = new StatusFilter();
        }
        return statusSolicitacao;
    }

    public void setStatusSolicitacao(StatusFilter statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }

    public LongFilter getSolicitacaoId() {
        return solicitacaoId;
    }

    public LongFilter solicitacaoId() {
        if (solicitacaoId == null) {
            solicitacaoId = new LongFilter();
        }
        return solicitacaoId;
    }

    public void setSolicitacaoId(LongFilter solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AvaliacaoSolicitacaoCriteria that = (AvaliacaoSolicitacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valorGasolina, that.valorGasolina) &&
            Objects.equals(totalGasto, that.totalGasto) &&
            Objects.equals(statusSolicitacao, that.statusSolicitacao) &&
            Objects.equals(solicitacaoId, that.solicitacaoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valorGasolina, totalGasto, statusSolicitacao, solicitacaoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvaliacaoSolicitacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valorGasolina != null ? "valorGasolina=" + valorGasolina + ", " : "") +
            (totalGasto != null ? "totalGasto=" + totalGasto + ", " : "") +
            (statusSolicitacao != null ? "statusSolicitacao=" + statusSolicitacao + ", " : "") +
            (solicitacaoId != null ? "solicitacaoId=" + solicitacaoId + ", " : "") +
            "}";
    }
}
