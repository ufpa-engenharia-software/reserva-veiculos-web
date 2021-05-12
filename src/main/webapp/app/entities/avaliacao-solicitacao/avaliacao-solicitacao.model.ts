import { ISolicitacao } from 'app/entities/solicitacao/solicitacao.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IAvaliacaoSolicitacao {
  id?: number;
  valorGasolina?: number | null;
  totalGasto?: number | null;
  statusSolicitacao?: Status | null;
  justificativaStatus?: string | null;
  solicitacao?: ISolicitacao | null;
}

export class AvaliacaoSolicitacao implements IAvaliacaoSolicitacao {
  constructor(
    public id?: number,
    public valorGasolina?: number | null,
    public totalGasto?: number | null,
    public statusSolicitacao?: Status | null,
    public justificativaStatus?: string | null,
    public solicitacao?: ISolicitacao | null
  ) {}
}

export function getAvaliacaoSolicitacaoIdentifier(avaliacaoSolicitacao: IAvaliacaoSolicitacao): number | undefined {
  return avaliacaoSolicitacao.id;
}
