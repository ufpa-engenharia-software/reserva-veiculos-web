package br.ufpa.facomp.veiculos.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.Veiculo} entity.
 */
public class VeiculoDTO implements Serializable {

    private Long id;

    @NotNull
    private String placa;

    private String modelo;

    private Integer ano;

    private Boolean disponivel;

    private ZonedDateTime criado;

    private FabricanteDTO fabricante;

    private CategoriaVeiculoDTO categoria;

    private Set<UsuarioDTO> motoristasHabilitados = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public FabricanteDTO getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteDTO fabricante) {
        this.fabricante = fabricante;
    }

    public CategoriaVeiculoDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculoDTO categoria) {
        this.categoria = categoria;
    }

    public Set<UsuarioDTO> getMotoristasHabilitados() {
        return motoristasHabilitados;
    }

    public void setMotoristasHabilitados(Set<UsuarioDTO> motoristasHabilitados) {
        this.motoristasHabilitados = motoristasHabilitados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VeiculoDTO)) {
            return false;
        }

        VeiculoDTO veiculoDTO = (VeiculoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, veiculoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VeiculoDTO{" +
            "id=" + getId() +
            ", placa='" + getPlaca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", ano=" + getAno() +
            ", disponivel='" + getDisponivel() + "'" +
            ", criado='" + getCriado() + "'" +
            ", fabricante=" + getFabricante() +
            ", categoria=" + getCategoria() +
            ", motoristasHabilitados=" + getMotoristasHabilitados() +
            "}";
    }
}
