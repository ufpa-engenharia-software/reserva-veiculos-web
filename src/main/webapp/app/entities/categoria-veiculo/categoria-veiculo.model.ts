import { NivelCNH } from 'app/entities/enumerations/nivel-cnh.model';

export interface ICategoriaVeiculo {
  id?: number;
  nome?: string | null;
  capacidadePessoas?: number | null;
  capacidadePeso?: number | null;
  capacidadeArea?: number | null;
  eixos?: number | null;
  altura?: number | null;
  nivelCNH?: NivelCNH | null;
}

export class CategoriaVeiculo implements ICategoriaVeiculo {
  constructor(
    public id?: number,
    public nome?: string | null,
    public capacidadePessoas?: number | null,
    public capacidadePeso?: number | null,
    public capacidadeArea?: number | null,
    public eixos?: number | null,
    public altura?: number | null,
    public nivelCNH?: NivelCNH | null
  ) {}
}

export function getCategoriaVeiculoIdentifier(categoriaVeiculo: ICategoriaVeiculo): number | undefined {
  return categoriaVeiculo.id;
}
