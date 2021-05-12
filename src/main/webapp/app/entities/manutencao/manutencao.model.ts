import { IVeiculo } from 'app/entities/veiculo/veiculo.model';

export interface IManutencao {
  id?: number;
  km?: number | null;
  descricao?: string | null;
  custo?: number | null;
  veiculo?: IVeiculo | null;
}

export class Manutencao implements IManutencao {
  constructor(
    public id?: number,
    public km?: number | null,
    public descricao?: string | null,
    public custo?: number | null,
    public veiculo?: IVeiculo | null
  ) {}
}

export function getManutencaoIdentifier(manutencao: IManutencao): number | undefined {
  return manutencao.id;
}
