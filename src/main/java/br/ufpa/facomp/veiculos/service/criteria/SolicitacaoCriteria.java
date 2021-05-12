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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.Solicitacao} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.SolicitacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /solicitacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SolicitacaoCriteria implements Serializable, Criteria {

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

    private StringFilter origem;

    private StringFilter destino;

    private ZonedDateTimeFilter dataSolicitacao;

    private ZonedDateTimeFilter horarioSaida;

    private ZonedDateTimeFilter horarioRetorno;

    private DoubleFilter distanciaEstimadaKm;

    private StatusFilter status;

    private IntegerFilter nPessoas;

    private DoubleFilter peso;

    private LongFilter categoriaId;

    private LongFilter veiculoAlocadoId;

    private LongFilter avaliacaoId;

    private LongFilter solicitanteId;

    private LongFilter autorizadorId;

    private LongFilter motoristaId;

    public SolicitacaoCriteria() {}

    public SolicitacaoCriteria(SolicitacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.origem = other.origem == null ? null : other.origem.copy();
        this.destino = other.destino == null ? null : other.destino.copy();
        this.dataSolicitacao = other.dataSolicitacao == null ? null : other.dataSolicitacao.copy();
        this.horarioSaida = other.horarioSaida == null ? null : other.horarioSaida.copy();
        this.horarioRetorno = other.horarioRetorno == null ? null : other.horarioRetorno.copy();
        this.distanciaEstimadaKm = other.distanciaEstimadaKm == null ? null : other.distanciaEstimadaKm.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.nPessoas = other.nPessoas == null ? null : other.nPessoas.copy();
        this.peso = other.peso == null ? null : other.peso.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.veiculoAlocadoId = other.veiculoAlocadoId == null ? null : other.veiculoAlocadoId.copy();
        this.avaliacaoId = other.avaliacaoId == null ? null : other.avaliacaoId.copy();
        this.solicitanteId = other.solicitanteId == null ? null : other.solicitanteId.copy();
        this.autorizadorId = other.autorizadorId == null ? null : other.autorizadorId.copy();
        this.motoristaId = other.motoristaId == null ? null : other.motoristaId.copy();
    }

    @Override
    public SolicitacaoCriteria copy() {
        return new SolicitacaoCriteria(this);
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

    public StringFilter getOrigem() {
        return origem;
    }

    public StringFilter origem() {
        if (origem == null) {
            origem = new StringFilter();
        }
        return origem;
    }

    public void setOrigem(StringFilter origem) {
        this.origem = origem;
    }

    public StringFilter getDestino() {
        return destino;
    }

    public StringFilter destino() {
        if (destino == null) {
            destino = new StringFilter();
        }
        return destino;
    }

    public void setDestino(StringFilter destino) {
        this.destino = destino;
    }

    public ZonedDateTimeFilter getDataSolicitacao() {
        return dataSolicitacao;
    }

    public ZonedDateTimeFilter dataSolicitacao() {
        if (dataSolicitacao == null) {
            dataSolicitacao = new ZonedDateTimeFilter();
        }
        return dataSolicitacao;
    }

    public void setDataSolicitacao(ZonedDateTimeFilter dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public ZonedDateTimeFilter getHorarioSaida() {
        return horarioSaida;
    }

    public ZonedDateTimeFilter horarioSaida() {
        if (horarioSaida == null) {
            horarioSaida = new ZonedDateTimeFilter();
        }
        return horarioSaida;
    }

    public void setHorarioSaida(ZonedDateTimeFilter horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public ZonedDateTimeFilter getHorarioRetorno() {
        return horarioRetorno;
    }

    public ZonedDateTimeFilter horarioRetorno() {
        if (horarioRetorno == null) {
            horarioRetorno = new ZonedDateTimeFilter();
        }
        return horarioRetorno;
    }

    public void setHorarioRetorno(ZonedDateTimeFilter horarioRetorno) {
        this.horarioRetorno = horarioRetorno;
    }

    public DoubleFilter getDistanciaEstimadaKm() {
        return distanciaEstimadaKm;
    }

    public DoubleFilter distanciaEstimadaKm() {
        if (distanciaEstimadaKm == null) {
            distanciaEstimadaKm = new DoubleFilter();
        }
        return distanciaEstimadaKm;
    }

    public void setDistanciaEstimadaKm(DoubleFilter distanciaEstimadaKm) {
        this.distanciaEstimadaKm = distanciaEstimadaKm;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getnPessoas() {
        return nPessoas;
    }

    public IntegerFilter nPessoas() {
        if (nPessoas == null) {
            nPessoas = new IntegerFilter();
        }
        return nPessoas;
    }

    public void setnPessoas(IntegerFilter nPessoas) {
        this.nPessoas = nPessoas;
    }

    public DoubleFilter getPeso() {
        return peso;
    }

    public DoubleFilter peso() {
        if (peso == null) {
            peso = new DoubleFilter();
        }
        return peso;
    }

    public void setPeso(DoubleFilter peso) {
        this.peso = peso;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LongFilter getVeiculoAlocadoId() {
        return veiculoAlocadoId;
    }

    public LongFilter veiculoAlocadoId() {
        if (veiculoAlocadoId == null) {
            veiculoAlocadoId = new LongFilter();
        }
        return veiculoAlocadoId;
    }

    public void setVeiculoAlocadoId(LongFilter veiculoAlocadoId) {
        this.veiculoAlocadoId = veiculoAlocadoId;
    }

    public LongFilter getAvaliacaoId() {
        return avaliacaoId;
    }

    public LongFilter avaliacaoId() {
        if (avaliacaoId == null) {
            avaliacaoId = new LongFilter();
        }
        return avaliacaoId;
    }

    public void setAvaliacaoId(LongFilter avaliacaoId) {
        this.avaliacaoId = avaliacaoId;
    }

    public LongFilter getSolicitanteId() {
        return solicitanteId;
    }

    public LongFilter solicitanteId() {
        if (solicitanteId == null) {
            solicitanteId = new LongFilter();
        }
        return solicitanteId;
    }

    public void setSolicitanteId(LongFilter solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public LongFilter getAutorizadorId() {
        return autorizadorId;
    }

    public LongFilter autorizadorId() {
        if (autorizadorId == null) {
            autorizadorId = new LongFilter();
        }
        return autorizadorId;
    }

    public void setAutorizadorId(LongFilter autorizadorId) {
        this.autorizadorId = autorizadorId;
    }

    public LongFilter getMotoristaId() {
        return motoristaId;
    }

    public LongFilter motoristaId() {
        if (motoristaId == null) {
            motoristaId = new LongFilter();
        }
        return motoristaId;
    }

    public void setMotoristaId(LongFilter motoristaId) {
        this.motoristaId = motoristaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SolicitacaoCriteria that = (SolicitacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(origem, that.origem) &&
            Objects.equals(destino, that.destino) &&
            Objects.equals(dataSolicitacao, that.dataSolicitacao) &&
            Objects.equals(horarioSaida, that.horarioSaida) &&
            Objects.equals(horarioRetorno, that.horarioRetorno) &&
            Objects.equals(distanciaEstimadaKm, that.distanciaEstimadaKm) &&
            Objects.equals(status, that.status) &&
            Objects.equals(nPessoas, that.nPessoas) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(veiculoAlocadoId, that.veiculoAlocadoId) &&
            Objects.equals(avaliacaoId, that.avaliacaoId) &&
            Objects.equals(solicitanteId, that.solicitanteId) &&
            Objects.equals(autorizadorId, that.autorizadorId) &&
            Objects.equals(motoristaId, that.motoristaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            origem,
            destino,
            dataSolicitacao,
            horarioSaida,
            horarioRetorno,
            distanciaEstimadaKm,
            status,
            nPessoas,
            peso,
            categoriaId,
            veiculoAlocadoId,
            avaliacaoId,
            solicitanteId,
            autorizadorId,
            motoristaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (origem != null ? "origem=" + origem + ", " : "") +
            (destino != null ? "destino=" + destino + ", " : "") +
            (dataSolicitacao != null ? "dataSolicitacao=" + dataSolicitacao + ", " : "") +
            (horarioSaida != null ? "horarioSaida=" + horarioSaida + ", " : "") +
            (horarioRetorno != null ? "horarioRetorno=" + horarioRetorno + ", " : "") +
            (distanciaEstimadaKm != null ? "distanciaEstimadaKm=" + distanciaEstimadaKm + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (nPessoas != null ? "nPessoas=" + nPessoas + ", " : "") +
            (peso != null ? "peso=" + peso + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (veiculoAlocadoId != null ? "veiculoAlocadoId=" + veiculoAlocadoId + ", " : "") +
            (avaliacaoId != null ? "avaliacaoId=" + avaliacaoId + ", " : "") +
            (solicitanteId != null ? "solicitanteId=" + solicitanteId + ", " : "") +
            (autorizadorId != null ? "autorizadorId=" + autorizadorId + ", " : "") +
            (motoristaId != null ? "motoristaId=" + motoristaId + ", " : "") +
            "}";
    }
}
