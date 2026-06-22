import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';

export const routes: Routes = [
    // Here the login route is set to the default when someone opens the app
    {path:'login', component:Login},
    {path:'', redirectTo:'login', pathMatch:"full"},
    
    // Currently this register route is the only functional part of the app
    {path:'register', component:Register}
];
