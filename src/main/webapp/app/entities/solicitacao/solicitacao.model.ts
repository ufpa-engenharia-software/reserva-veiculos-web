import * as dayjs from 'dayjs';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { IAvaliacaoSolicitacao } from 'app/entities/avaliacao-solicitacao/avaliacao-solicitacao.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ISolicitacao {
  id?: number;
  origem?: string | null;
  destino?: string | null;
  dataSolicitacao?: dayjs.Dayjs | null;
  horarioSaida?: dayjs.Dayjs | null;
  horarioRetorno?: dayjs.Dayjs | null;
  distanciaEstimadaKm?: number | null;
  justificativa?: string | null;
  status?: Status | null;
  nPessoas?: number | null;
  peso?: number | null;
  categoria?: ICategoriaVeiculo | null;
  veiculoAlocado?: IVeiculo | null;
  avaliacao?: IAvaliacaoSolicitacao | null;
  solicitante?: IUsuario | null;
  autorizador?: IUsuario | null;
  motorista?: IUsuario | null;
}

export class Solicitacao implements ISolicitacao {
  constructor(
    public id?: number,
    public origem?: string | null,
    public destino?: string | null,
    public dataSolicitacao?: dayjs.Dayjs | null,
    public horarioSaida?: dayjs.Dayjs | null,
    public horarioRetorno?: dayjs.Dayjs | null,
    public distanciaEstimadaKm?: number | null,
    public justificativa?: string | null,
    public status?: Status | null,
    public nPessoas?: number | null,
    public peso?: number | null,
    public categoria?: ICategoriaVeiculo | null,
    public veiculoAlocado?: IVeiculo | null,
    public avaliacao?: IAvaliacaoSolicitacao | null,
    public solicitante?: IUsuario | null,
    public autorizador?: IUsuario | null,
    public motorista?: IUsuario | null
  ) {}
}

export function getSolicitacaoIdentifier(solicitacao: ISolicitacao): number | undefined {
  return solicitacao.id;
}
