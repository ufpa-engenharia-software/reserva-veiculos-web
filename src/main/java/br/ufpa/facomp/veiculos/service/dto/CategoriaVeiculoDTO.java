package br.ufpa.facomp.veiculos.service.dto;

import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.CategoriaVeiculo} entity.
 */
public class CategoriaVeiculoDTO implements Serializable {

    private Long id;

    private String nome;

    private Integer capacidadePessoas;

    private Double capacidadePeso;

    private Double capacidadeArea;

    private Integer eixos;

    private Double altura;

    private NivelCNH nivelCNH;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidadePessoas() {
        return capacidadePessoas;
    }

    public void setCapacidadePessoas(Integer capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
    }

    public Double getCapacidadePeso() {
        return capacidadePeso;
    }

    public void setCapacidadePeso(Double capacidadePeso) {
        this.capacidadePeso = capacidadePeso;
    }

    public Double getCapacidadeArea() {
        return capacidadeArea;
    }

    public void setCapacidadeArea(Double capacidadeArea) {
        this.capacidadeArea = capacidadeArea;
    }

    public Integer getEixos() {
        return eixos;
    }

    public void setEixos(Integer eixos) {
        this.eixos = eixos;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public NivelCNH getNivelCNH() {
        return nivelCNH;
    }

    public void setNivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaVeiculoDTO)) {
            return false;
        }

        CategoriaVeiculoDTO categoriaVeiculoDTO = (CategoriaVeiculoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaVeiculoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaVeiculoDTO{" +
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
