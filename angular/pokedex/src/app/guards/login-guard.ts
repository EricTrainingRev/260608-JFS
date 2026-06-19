import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../services/login-service';

/*
  Route guards are funtions that are provided a route and state argument
  when they are invoked: route provides details about the navigation in
  progress, state provides metadata about the navigation

  The way these guards work is relatively simple: if a true is returned
  the navigation goes through. If true is not returned you need to provide
  instructions for how the navigation should be handled instead
*/
export const loginGuard: CanActivateFn = (route, state) => {

  // for this example we can check if the user is logged in through the LoginService
  // note this is function scope, so we need the variable declaration keyword
  const loginService = inject(LoginService)
  const router = inject(Router)

  if(loginService.checkIfUserIsLoggedIn()){
    // if the user is logged in the proceed with no problems
    return true;
  }
  // otherwise the user is not logged in and needs to be sent to the login page
  return router.navigate(["/login"]);
};
