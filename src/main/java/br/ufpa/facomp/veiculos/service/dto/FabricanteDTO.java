package br.ufpa.facomp.veiculos.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.Fabricante} entity.
 */
public class FabricanteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private ZonedDateTime criado;

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

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FabricanteDTO)) {
            return false;
        }

        FabricanteDTO fabricanteDTO = (FabricanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fabricanteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FabricanteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
