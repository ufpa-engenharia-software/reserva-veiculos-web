import * as dayjs from 'dayjs';

export interface IFabricante {
  id?: number;
  nome?: string;
  criado?: dayjs.Dayjs | null;
}

export class Fabricante implements IFabricante {
  constructor(public id?: number, public nome?: string, public criado?: dayjs.Dayjs | null) {}
}

export function getFabricanteIdentifier(fabricante: IFabricante): number | undefined {
  return fabricante.id;
}
