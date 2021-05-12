package br.ufpa.facomp.veiculos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.Manutencao} entity.
 */
public class ManutencaoDTO implements Serializable {

    private Long id;

    private Double km;

    @Lob
    private String descricao;

    private Double custo;

    private VeiculoDTO veiculo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public VeiculoDTO getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(VeiculoDTO veiculo) {
        this.veiculo = veiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManutencaoDTO)) {
            return false;
        }

        ManutencaoDTO manutencaoDTO = (ManutencaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manutencaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManutencaoDTO{" +
            "id=" + getId() +
            ", km=" + getKm() +
            ", descricao='" + getDescricao() + "'" +
            ", custo=" + getCusto() +
            ", veiculo=" + getVeiculo() +
            "}";
    }
}
