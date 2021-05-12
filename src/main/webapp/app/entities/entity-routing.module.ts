import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'usuario',
        data: { pageTitle: 'reservaVeiculosApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'fabricante',
        data: { pageTitle: 'reservaVeiculosApp.fabricante.home.title' },
        loadChildren: () => import('./fabricante/fabricante.module').then(m => m.FabricanteModule),
      },
      {
        path: 'veiculo',
        data: { pageTitle: 'reservaVeiculosApp.veiculo.home.title' },
        loadChildren: () => import('./veiculo/veiculo.module').then(m => m.VeiculoModule),
      },
      {
        path: 'manutencao',
        data: { pageTitle: 'reservaVeiculosApp.manutencao.home.title' },
        loadChildren: () => import('./manutencao/manutencao.module').then(m => m.ManutencaoModule),
      },
      {
        path: 'categoria-veiculo',
        data: { pageTitle: 'reservaVeiculosApp.categoriaVeiculo.home.title' },
        loadChildren: () => import('./categoria-veiculo/categoria-veiculo.module').then(m => m.CategoriaVeiculoModule),
      },
      {
        path: 'solicitacao',
        data: { pageTitle: 'reservaVeiculosApp.solicitacao.home.title' },
        loadChildren: () => import('./solicitacao/solicitacao.module').then(m => m.SolicitacaoModule),
      },
      {
        path: 'avaliacao-solicitacao',
        data: { pageTitle: 'reservaVeiculosApp.avaliacaoSolicitacao.home.title' },
        loadChildren: () => import('./avaliacao-solicitacao/avaliacao-solicitacao.module').then(m => m.AvaliacaoSolicitacaoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
