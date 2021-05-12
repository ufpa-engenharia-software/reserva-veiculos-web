package br.ufpa.facomp.veiculos.service.dto;

import br.ufpa.facomp.veiculos.domain.enumeration.Identificador;
import br.ufpa.facomp.veiculos.domain.enumeration.NivelCNH;
import br.ufpa.facomp.veiculos.domain.enumeration.PerfilUsuario;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.Usuario} entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private PerfilUsuario perfil;

    private Identificador identificacao;

    private String nidentificao;

    private String cpf;

    private String email;

    private String celular;

    private Boolean whatsapp;

    private Boolean ativo;

    private ZonedDateTime criado;

    private NivelCNH nivelCNH;

    private UserDTO user;

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

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public Identificador getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(Identificador identificacao) {
        this.identificacao = identificacao;
    }

    public String getNidentificao() {
        return nidentificao;
    }

    public void setNidentificao(String nidentificao) {
        this.nidentificao = nidentificao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Boolean getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public NivelCNH getNivelCNH() {
        return nivelCNH;
    }

    public void setNivelCNH(NivelCNH nivelCNH) {
        this.nivelCNH = nivelCNH;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioDTO)) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", perfil='" + getPerfil() + "'" +
            ", identificacao='" + getIdentificacao() + "'" +
            ", nidentificao='" + getNidentificao() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", email='" + getEmail() + "'" +
            ", celular='" + getCelular() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", criado='" + getCriado() + "'" +
            ", nivelCNH='" + getNivelCNH() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
