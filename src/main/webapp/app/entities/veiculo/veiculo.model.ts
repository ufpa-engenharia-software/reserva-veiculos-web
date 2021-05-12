import * as dayjs from 'dayjs';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IVeiculo {
  id?: number;
  placa?: string;
  modelo?: string | null;
  ano?: number | null;
  disponivel?: boolean | null;
  criado?: dayjs.Dayjs | null;
  fabricante?: IFabricante | null;
  categoria?: ICategoriaVeiculo | null;
  motoristasHabilitados?: IUsuario[] | null;
}

export class Veiculo implements IVeiculo {
  constructor(
    public id?: number,
    public placa?: string,
    public modelo?: string | null,
    public ano?: number | null,
    public disponivel?: boolean | null,
    public criado?: dayjs.Dayjs | null,
    public fabricante?: IFabricante | null,
    public categoria?: ICategoriaVeiculo | null,
    public motoristasHabilitados?: IUsuario[] | null
  ) {
    this.disponivel = this.disponivel ?? false;
  }
}

export function getVeiculoIdentifier(veiculo: IVeiculo): number | undefined {
  return veiculo.id;
}
