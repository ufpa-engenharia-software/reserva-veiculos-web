package br.ufpa.facomp.veiculos.service.criteria;

import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
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
 * Criteria class for the {@link br.ufpa.facomp.veiculos.domain.CategoriaVeiculo} entity. This class is used
 * in {@link br.ufpa.facomp.veiculos.web.rest.CategoriaVeiculoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categoria-veiculos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoriaVeiculoCriteria implements Serializable, Criteria {

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

    private IntegerFilter capacidadePessoas;

    private DoubleFilter capacidadePeso;

    private DoubleFilter capacidadeArea;

    private IntegerFilter eixos;

    private DoubleFilter altura;

    private NivelCNHFilter nivelCNH;

    public CategoriaVeiculoCriteria() {}

    public CategoriaVeiculoCriteria(CategoriaVeiculoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.capacidadePessoas = other.capacidadePessoas == null ? null : other.capacidadePessoas.copy();
        this.capacidadePeso = other.capacidadePeso == null ? null : other.capacidadePeso.copy();
        this.capacidadeArea = other.capacidadeArea == null ? null : other.capacidadeArea.copy();
        this.eixos = other.eixos == null ? null : other.eixos.copy();
        this.altura = other.altura == null ? null : other.altura.copy();
        this.nivelCNH = other.nivelCNH == null ? null : other.nivelCNH.copy();
    }

    @Override
    public CategoriaVeiculoCriteria copy() {
        return new CategoriaVeiculoCriteria(this);
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

    public IntegerFilter getCapacidadePessoas() {
        return capacidadePessoas;
    }

    public IntegerFilter capacidadePessoas() {
        if (capacidadePessoas == null) {
            capacidadePessoas = new IntegerFilter();
        }
        return capacidadePessoas;
    }

    public void setCapacidadePessoas(IntegerFilter capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
    }

    public DoubleFilter getCapacidadePeso() {
        return capacidadePeso;
    }

    public DoubleFilter capacidadePeso() {
        if (capacidadePeso == null) {
            capacidadePeso = new DoubleFilter();
        }
        return capacidadePeso;
    }

    public void setCapacidadePeso(DoubleFilter capacidadePeso) {
        this.capacidadePeso = capacidadePeso;
    }

    public DoubleFilter getCapacidadeArea() {
        return capacidadeArea;
    }

    public DoubleFilter capacidadeArea() {
        if (capacidadeArea == null) {
            capacidadeArea = new DoubleFilter();
        }
        return capacidadeArea;
    }

    public void setCapacidadeArea(DoubleFilter capacidadeArea) {
        this.capacidadeArea = capacidadeArea;
    }

    public IntegerFilter getEixos() {
        return eixos;
    }

    public IntegerFilter eixos() {
        if (eixos == null) {
            eixos = new IntegerFilter();
        }
        return eixos;
    }

    public void setEixos(IntegerFilter eixos) {
        this.eixos = eixos;
    }

    public DoubleFilter getAltura() {
        return altura;
    }

    public DoubleFilter altura() {
        if (altura == null) {
            altura = new DoubleFilter();
        }
        return altura;
    }

    public void setAltura(DoubleFilter altura) {
        this.altura = altura;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoriaVeiculoCriteria that = (CategoriaVeiculoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(capacidadePessoas, that.capacidadePessoas) &&
            Objects.equals(capacidadePeso, that.capacidadePeso) &&
            Objects.equals(capacidadeArea, that.capacidadeArea) &&
            Objects.equals(eixos, that.eixos) &&
            Objects.equals(altura, that.altura) &&
            Objects.equals(nivelCNH, that.nivelCNH)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, capacidadePessoas, capacidadePeso, capacidadeArea, eixos, altura, nivelCNH);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaVeiculoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (capacidadePessoas != null ? "capacidadePessoas=" + capacidadePessoas + ", " : "") +
            (capacidadePeso != null ? "capacidadePeso=" + capacidadePeso + ", " : "") +
            (capacidadeArea != null ? "capacidadeArea=" + capacidadeArea + ", " : "") +
            (eixos != null ? "eixos=" + eixos + ", " : "") +
            (altura != null ? "altura=" + altura + ", " : "") +
            (nivelCNH != null ? "nivelCNH=" + nivelCNH + ", " : "") +
            "}";
    }
}
