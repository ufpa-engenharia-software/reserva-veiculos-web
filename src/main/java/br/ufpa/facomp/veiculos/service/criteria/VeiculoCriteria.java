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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.Veiculo} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.VeiculoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /veiculos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VeiculoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter placa;

    private StringFilter modelo;

    private IntegerFilter ano;

    private BooleanFilter disponivel;

    private ZonedDateTimeFilter criado;

    private LongFilter fabricanteId;

    private LongFilter categoriaId;

    private LongFilter motoristasHabilitadosId;

    public VeiculoCriteria() {}

    public VeiculoCriteria(VeiculoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.placa = other.placa == null ? null : other.placa.copy();
        this.modelo = other.modelo == null ? null : other.modelo.copy();
        this.ano = other.ano == null ? null : other.ano.copy();
        this.disponivel = other.disponivel == null ? null : other.disponivel.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.fabricanteId = other.fabricanteId == null ? null : other.fabricanteId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.motoristasHabilitadosId = other.motoristasHabilitadosId == null ? null : other.motoristasHabilitadosId.copy();
    }

    @Override
    public VeiculoCriteria copy() {
        return new VeiculoCriteria(this);
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

    public StringFilter getPlaca() {
        return placa;
    }

    public StringFilter placa() {
        if (placa == null) {
            placa = new StringFilter();
        }
        return placa;
    }

    public void setPlaca(StringFilter placa) {
        this.placa = placa;
    }

    public StringFilter getModelo() {
        return modelo;
    }

    public StringFilter modelo() {
        if (modelo == null) {
            modelo = new StringFilter();
        }
        return modelo;
    }

    public void setModelo(StringFilter modelo) {
        this.modelo = modelo;
    }

    public IntegerFilter getAno() {
        return ano;
    }

    public IntegerFilter ano() {
        if (ano == null) {
            ano = new IntegerFilter();
        }
        return ano;
    }

    public void setAno(IntegerFilter ano) {
        this.ano = ano;
    }

    public BooleanFilter getDisponivel() {
        return disponivel;
    }

    public BooleanFilter disponivel() {
        if (disponivel == null) {
            disponivel = new BooleanFilter();
        }
        return disponivel;
    }

    public void setDisponivel(BooleanFilter disponivel) {
        this.disponivel = disponivel;
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

    public LongFilter getFabricanteId() {
        return fabricanteId;
    }

    public LongFilter fabricanteId() {
        if (fabricanteId == null) {
            fabricanteId = new LongFilter();
        }
        return fabricanteId;
    }

    public void setFabricanteId(LongFilter fabricanteId) {
        this.fabricanteId = fabricanteId;
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

    public LongFilter getMotoristasHabilitadosId() {
        return motoristasHabilitadosId;
    }

    public LongFilter motoristasHabilitadosId() {
        if (motoristasHabilitadosId == null) {
            motoristasHabilitadosId = new LongFilter();
        }
        return motoristasHabilitadosId;
    }

    public void setMotoristasHabilitadosId(LongFilter motoristasHabilitadosId) {
        this.motoristasHabilitadosId = motoristasHabilitadosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VeiculoCriteria that = (VeiculoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(placa, that.placa) &&
            Objects.equals(modelo, that.modelo) &&
            Objects.equals(ano, that.ano) &&
            Objects.equals(disponivel, that.disponivel) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(fabricanteId, that.fabricanteId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(motoristasHabilitadosId, that.motoristasHabilitadosId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa, modelo, ano, disponivel, criado, fabricanteId, categoriaId, motoristasHabilitadosId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VeiculoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (placa != null ? "placa=" + placa + ", " : "") +
            (modelo != null ? "modelo=" + modelo + ", " : "") +
            (ano != null ? "ano=" + ano + ", " : "") +
            (disponivel != null ? "disponivel=" + disponivel + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (fabricanteId != null ? "fabricanteId=" + fabricanteId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (motoristasHabilitadosId != null ? "motoristasHabilitadosId=" + motoristasHabilitadosId + ", " : "") +
            "}";
    }
}
