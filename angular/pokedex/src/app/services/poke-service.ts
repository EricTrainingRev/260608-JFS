import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

/*
  This service will serve two purposes for us:
  1. It will hold the code to actually query the PokeAPI and retrieve pokemon
     information for us
  2. It will store the pokemon information retrieved and provide a central
     location for all components to access the pokemon data that need it
*/

@Injectable({
  providedIn: 'root',
})
export class PokeService {

  private httpClient = inject(HttpClient)

  queryPokemon(identifier: string){
    
  }

}
