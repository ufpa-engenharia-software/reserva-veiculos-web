package br.ufpa.facomp.veiculos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Manutencao.
 */
@Entity
@Table(name = "manutencao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Manutencao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "km")
    private Double km;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "custo")
    private Double custo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fabricante", "categoria", "motoristasHabilitados" }, allowSetters = true)
    private Veiculo veiculo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manutencao id(Long id) {
        this.id = id;
        return this;
    }

    public Double getKm() {
        return this.km;
    }

    public Manutencao km(Double km) {
        this.km = km;
        return this;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Manutencao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getCusto() {
        return this.custo;
    }

    public Manutencao custo(Double custo) {
        this.custo = custo;
        return this;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    public Manutencao veiculo(Veiculo veiculo) {
        this.setVeiculo(veiculo);
        return this;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manutencao)) {
            return false;
        }
        return id != null && id.equals(((Manutencao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manutencao{" +
            "id=" + getId() +
            ", km=" + getKm() +
            ", descricao='" + getDescricao() + "'" +
            ", custo=" + getCusto() +
            "}";
    }
}
