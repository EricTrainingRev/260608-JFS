import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./component/login/login').then((m) => m.Login),
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./pages/dashboard/dashboard-page').then((m) => m.DashboardPage),
  },
  {
    path: '**',
    loadComponent: () =>
      import('./pages/page-not-found/page-not-found').then((m) => m.PageNotFound),
  }
];
