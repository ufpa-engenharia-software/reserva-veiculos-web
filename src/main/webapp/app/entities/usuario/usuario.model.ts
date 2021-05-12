import * as dayjs from 'dayjs';
import { ISolicitacao } from 'app/entities/solicitacao/solicitacao.model';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { PerfilUsuario } from 'app/entities/enumerations/perfil-usuario.model';
import { Identificador } from 'app/entities/enumerations/identificador.model';
import { NivelCNH } from 'app/entities/enumerations/nivel-cnh.model';

export interface IUsuario {
  id?: number;
  nome?: string;
  perfil?: PerfilUsuario | null;
  identificacao?: Identificador | null;
  nidentificao?: string | null;
  cpf?: string | null;
  email?: string | null;
  celular?: string | null;
  whatsapp?: boolean | null;
  ativo?: boolean | null;
  criado?: dayjs.Dayjs | null;
  nivelCNH?: NivelCNH | null;
  minhasSolicitacoes?: ISolicitacao[] | null;
  comoAutorizadors?: ISolicitacao[] | null;
  comoMotoristas?: ISolicitacao[] | null;
  veiculosHabilitados?: IVeiculo[] | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public nome?: string,
    public perfil?: PerfilUsuario | null,
    public identificacao?: Identificador | null,
    public nidentificao?: string | null,
    public cpf?: string | null,
    public email?: string | null,
    public celular?: string | null,
    public whatsapp?: boolean | null,
    public ativo?: boolean | null,
    public criado?: dayjs.Dayjs | null,
    public nivelCNH?: NivelCNH | null,
    public minhasSolicitacoes?: ISolicitacao[] | null,
    public comoAutorizadors?: ISolicitacao[] | null,
    public comoMotoristas?: ISolicitacao[] | null,
    public veiculosHabilitados?: IVeiculo[] | null
  ) {
    this.whatsapp = this.whatsapp ?? false;
    this.ativo = this.ativo ?? false;
  }
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
