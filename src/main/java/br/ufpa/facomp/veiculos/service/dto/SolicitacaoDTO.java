package br.ufpa.facomp.veiculos.service.dto;

import br.ufpa.facomp.veiculos.domain.enumeration.Status;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.facomp.veiculos.domain.Solicitacao} entity.
 */
public class SolicitacaoDTO implements Serializable {

    private Long id;

    private String origem;

    private String destino;

    private ZonedDateTime dataSolicitacao;

    private ZonedDateTime horarioSaida;

    private ZonedDateTime horarioRetorno;

    private Double distanciaEstimadaKm;

    @Lob
    private String justificativa;

    private Status status;

    private Integer nPessoas;

    private Double peso;

    private CategoriaVeiculoDTO categoria;

    private VeiculoDTO veiculoAlocado;

    private UsuarioDTO solicitante;

    private UsuarioDTO autorizador;

    private UsuarioDTO motorista;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public ZonedDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(ZonedDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public ZonedDateTime getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(ZonedDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public ZonedDateTime getHorarioRetorno() {
        return horarioRetorno;
    }

    public void setHorarioRetorno(ZonedDateTime horarioRetorno) {
        this.horarioRetorno = horarioRetorno;
    }

    public Double getDistanciaEstimadaKm() {
        return distanciaEstimadaKm;
    }

    public void setDistanciaEstimadaKm(Double distanciaEstimadaKm) {
        this.distanciaEstimadaKm = distanciaEstimadaKm;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getnPessoas() {
        return nPessoas;
    }

    public void setnPessoas(Integer nPessoas) {
        this.nPessoas = nPessoas;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public CategoriaVeiculoDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculoDTO categoria) {
        this.categoria = categoria;
    }

    public VeiculoDTO getVeiculoAlocado() {
        return veiculoAlocado;
    }

    public void setVeiculoAlocado(VeiculoDTO veiculoAlocado) {
        this.veiculoAlocado = veiculoAlocado;
    }

    public UsuarioDTO getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UsuarioDTO solicitante) {
        this.solicitante = solicitante;
    }

    public UsuarioDTO getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(UsuarioDTO autorizador) {
        this.autorizador = autorizador;
    }

    public UsuarioDTO getMotorista() {
        return motorista;
    }

    public void setMotorista(UsuarioDTO motorista) {
        this.motorista = motorista;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitacaoDTO)) {
            return false;
        }

        SolicitacaoDTO solicitacaoDTO = (SolicitacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, solicitacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SolicitacaoDTO{" +
            "id=" + getId() +
            ", origem='" + getOrigem() + "'" +
            ", destino='" + getDestino() + "'" +
            ", dataSolicitacao='" + getDataSolicitacao() + "'" +
            ", horarioSaida='" + getHorarioSaida() + "'" +
            ", horarioRetorno='" + getHorarioRetorno() + "'" +
            ", distanciaEstimadaKm=" + getDistanciaEstimadaKm() +
            ", justificativa='" + getJustificativa() + "'" +
            ", status='" + getStatus() + "'" +
            ", nPessoas=" + getnPessoas() +
            ", peso=" + getPeso() +
            ", categoria=" + getCategoria() +
            ", veiculoAlocado=" + getVeiculoAlocado() +
            ", solicitante=" + getSolicitante() +
            ", autorizador=" + getAutorizador() +
            ", motorista=" + getMotorista() +
            "}";
    }
}
