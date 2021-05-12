package br.ufpa.facomp.veiculos.domain;

import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CategoriaVeiculo.
 */
@Entity
@Table(name = "categoria_veiculo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategoriaVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "capacidade_pessoas")
    private Integer capacidadePessoas;

    @Column(name = "capacidade_peso")
    private Double capacidadePeso;

    @Column(name = "capacidade_area")
    private Double capacidadeArea;

    @Column(name = "eixos")
    private Integer eixos;

    @Column(name = "altura")
    private Double altura;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_cnh")
    private NivelCNH nivelCNH;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoriaVeiculo id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public CategoriaVeiculo nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidadePessoas() {
        return this.capacidadePessoas;
    }

    public CategoriaVeiculo capacidadePessoas(Integer capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
        return this;
    }

    public void setCapacidadePessoas(Integer capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
    }

    public Double getCapacidadePeso() {
        return this.capacidadePeso;
    }

    public CategoriaVeiculo capacidadePeso(Double capacidadePeso) {
        this.capacidadePeso = capacidadePeso;
        return this;
    }

    public void setCapacidadePeso(Double capacidadePeso) {
        this.capacidadePeso = capacidadePeso;
    }

    public Double getCapacidadeArea() {
        return this.capacidadeArea;
    }

    public CategoriaVeiculo capacidadeArea(Double capacidadeArea) {
        this.capacidadeArea = capacidadeArea;
        return this;
    }

    public void setCapacidadeArea(Double capacidadeArea) {
        this.capacidadeArea = capacidadeArea;
    }

    public Integer getEixos() {
        return this.eixos;
    }

    public CategoriaVeiculo eixos(Integer eixos) {
        this.eixos = eixos;
        return this;
    }

    public void setEixos(Integer eixos) {
        this.eixos = eixos;
    }

    public Double getAltura() {
        return this.altura;
    }

    public CategoriaVeiculo altura(Double altura) {
        this.altura = altura;
        return this;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public NivelCNH getNivelCNH() {
        return this.nivelCNH;
    }

    public CategoriaVeiculo nivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
        return this;
    }

    public void setNivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaVeiculo)) {
            return false;
        }
        return id != null && id.equals(((CategoriaVeiculo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaVeiculo{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", capacidadePessoas=" + getCapacidadePessoas() +
            ", capacidadePeso=" + getCapacidadePeso() +
            ", capacidadeArea=" + getCapacidadeArea() +
            ", eixos=" + getEixos() +
            ", altura=" + getAltura() +
            ", nivelCNH='" + getNivelCNH() + "'" +
            "}";
    }
}
