package br.ufpa.facomp.veiculos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Veiculo.
 */
@Entity
@Table(name = "veiculo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "placa", nullable = false)
    private String placa;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "disponivel")
    private Boolean disponivel;

    @Column(name = "criado")
    private ZonedDateTime criado;

    @ManyToOne
    private Fabricante fabricante;

    @ManyToOne
    private CategoriaVeiculo categoria;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_veiculo__motoristas_habilitados",
        joinColumns = @JoinColumn(name = "veiculo_id"),
        inverseJoinColumns = @JoinColumn(name = "motoristas_habilitados_id")
    )
    @JsonIgnoreProperties(
        value = { "minhasSolicitacoes", "comoAutorizadors", "comoMotoristas", "veiculosHabilitados" },
        allowSetters = true
    )
    private Set<Usuario> motoristasHabilitados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo id(Long id) {
        this.id = id;
        return this;
    }

    public String getPlaca() {
        return this.placa;
    }

    public Veiculo placa(String placa) {
        this.placa = placa;
        return this;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Veiculo modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return this.ano;
    }

    public Veiculo ano(Integer ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean getDisponivel() {
        return this.disponivel;
    }

    public Veiculo disponivel(Boolean disponivel) {
        this.disponivel = disponivel;
        return this;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Veiculo criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Fabricante getFabricante() {
        return this.fabricante;
    }

    public Veiculo fabricante(Fabricante fabricante) {
        this.setFabricante(fabricante);
        return this;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public CategoriaVeiculo getCategoria() {
        return this.categoria;
    }

    public Veiculo categoria(CategoriaVeiculo categoriaVeiculo) {
        this.setCategoria(categoriaVeiculo);
        return this;
    }

    public void setCategoria(CategoriaVeiculo categoriaVeiculo) {
        this.categoria = categoriaVeiculo;
    }

    public Set<Usuario> getMotoristasHabilitados() {
        return this.motoristasHabilitados;
    }

    public Veiculo motoristasHabilitados(Set<Usuario> usuarios) {
        this.setMotoristasHabilitados(usuarios);
        return this;
    }

    public Veiculo addMotoristasHabilitados(Usuario usuario) {
        this.motoristasHabilitados.add(usuario);
        usuario.getVeiculosHabilitados().add(this);
        return this;
    }

    public Veiculo removeMotoristasHabilitados(Usuario usuario) {
        this.motoristasHabilitados.remove(usuario);
        usuario.getVeiculosHabilitados().remove(this);
        return this;
    }

    public void setMotoristasHabilitados(Set<Usuario> usuarios) {
        this.motoristasHabilitados = usuarios;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Veiculo)) {
            return false;
        }
        return id != null && id.equals(((Veiculo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Veiculo{" +
            "id=" + getId() +
            ", placa='" + getPlaca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", ano=" + getAno() +
            ", disponivel='" + getDisponivel() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
