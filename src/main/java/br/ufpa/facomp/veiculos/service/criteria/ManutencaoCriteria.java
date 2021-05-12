package br.ufpa.facomp.veiculos.service.criteria;

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
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.Manutencao} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.ManutencaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /manutencaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ManutencaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter km;

    private DoubleFilter custo;

    private LongFilter veiculoId;

    public ManutencaoCriteria() {}

    public ManutencaoCriteria(ManutencaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.km = other.km == null ? null : other.km.copy();
        this.custo = other.custo == null ? null : other.custo.copy();
        this.veiculoId = other.veiculoId == null ? null : other.veiculoId.copy();
    }

    @Override
    public ManutencaoCriteria copy() {
        return new ManutencaoCriteria(this);
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

    public DoubleFilter getKm() {
        return km;
    }

    public DoubleFilter km() {
        if (km == null) {
            km = new DoubleFilter();
        }
        return km;
    }

    public void setKm(DoubleFilter km) {
        this.km = km;
    }

    public DoubleFilter getCusto() {
        return custo;
    }

    public DoubleFilter custo() {
        if (custo == null) {
            custo = new DoubleFilter();
        }
        return custo;
    }

    public void setCusto(DoubleFilter custo) {
        this.custo = custo;
    }

    public LongFilter getVeiculoId() {
        return veiculoId;
    }

    public LongFilter veiculoId() {
        if (veiculoId == null) {
            veiculoId = new LongFilter();
        }
        return veiculoId;
    }

    public void setVeiculoId(LongFilter veiculoId) {
        this.veiculoId = veiculoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ManutencaoCriteria that = (ManutencaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(km, that.km) &&
            Objects.equals(custo, that.custo) &&
            Objects.equals(veiculoId, that.veiculoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, km, custo, veiculoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManutencaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (km != null ? "km=" + km + ", " : "") +
            (custo != null ? "custo=" + custo + ", " : "") +
            (veiculoId != null ? "veiculoId=" + veiculoId + ", " : "") +
            "}";
    }
}
