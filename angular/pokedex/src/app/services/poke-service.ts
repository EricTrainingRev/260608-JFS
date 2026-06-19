import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Pokemon } from '../interfaces/pokemon';
import { BehaviorSubject, catchError, Observable, of, tap } from 'rxjs';

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

  private pokeAPIUrl = "https://pokeapi.co/api/v2/pokemon/"

  private httpClient = inject(HttpClient)

  /*
    This is our internal storage for Pokemon data. Whenever we query new
    Pokemon data we will need to make sure that we store it here in our
    BehaviorSubject. This will allow us to "push" notifications of the 
    data changing to all interested components
  */
  private pokemonSubject: BehaviorSubject<Pokemon>

  // the common syntax for referencing a data stream (AKA Observable)
  // is to put a $ at the end of the reference name
  pokemon$: Observable<Pokemon>;

  // we will need an empty pokemon in our HttpClient as well, so we can define it here
  private emptyPokemon = {
      name: '',
      sprites: {
        back_default:'',
        front_default:''
      }
    }

  constructor(){
    this.pokemonSubject = new BehaviorSubject<Pokemon>(this.emptyPokemon);
    this.pokemon$ = this.pokemonSubject.asObservable();
  }

  queryPokemon(identifier: string){
    /*
      Unlike in raw JS when using the fetch function to query data and getting
      a Promise that must be unpacked back, the HttpClient will instead return
      an "Observable". Observables are objects provided by the RxJs library
      that allow for more powerful asynchronous programming. Unlike Promises
      that are "fire and forget" in that once triggered you will get a single
      resolve/reject result, Observables instead represent "streams" of data.

      While ultimately we are going to be using an Observable to store the
      Pokemon data and provide access to our interested components, the mechanism
      by which we will store the Pokemon data and update all interested components
      is through the use of a class called a "Subject".
    */
    this.httpClient.get<Pokemon>(this.pokeAPIUrl + identifier)
      // we can use the pipe() operator to perform actions on the data
      // returned by the get call
      .pipe(
        // we can use tap to tell our app "update our pokemon subject with the new data"
        tap(pokemonData => this.pokemonSubject.next(pokemonData)),
        // if something goes wrong the catchError function triggers
        catchError(error => {
          // we log what went wrong to the console
          console.log(`Something went wrong: ${error}`)
          // we update the internal storage to an empty pokemon object
          this.pokemonSubject.next(this.emptyPokemon);
          // catchError needs to return an Observable so we can call the subscribe function and actually 
          // trigger all the actions we have programmed here. Since we don't do anything with this
          // error observable we can make its data null
          return of(null);
        })
      ).subscribe() // to actually trigger the actions associated with an observable you must "subscribe" to it
  }

}
