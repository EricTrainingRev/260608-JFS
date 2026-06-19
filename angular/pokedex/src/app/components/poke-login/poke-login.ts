import { Component, inject } from '@angular/core';
import { LoginService } from '../../services/login-service';

@Component({
  selector: 'app-poke-login',
  imports: [],
  templateUrl: './poke-login.html',
  styleUrl: './poke-login.css',
})
export class PokeLogin {

  private loginService = inject(LoginService)

  login(){
    this.loginService.attemptLogin();
  }

}
