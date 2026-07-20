import { Component, Signal, signal, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PokeService } from '../../services/pokeservice';
import { Subscription } from 'rxjs';
import { Type } from "../../directives/type";
import { MoveTransformPipe } from '../../pipes/move-transform-pipe';
import { TitleCasePipe } from '@angular/common';

@Component({
  selector: 'app-monolith',
  imports: [FormsModule, MoveTransformPipe, Type, TitleCasePipe],
  templateUrl: './monolith.html',
  styleUrl: './monolith.css'
})
export class Monolith {

  pokemon: WritableSignal<Pokemon>;
  private subscription: Subscription

  headerText = signal("Welcome to the Pokedex!");
  instructions = signal("Enter the name of a Pokemon to look up its stats")

  pokeSearch = ""

  pokemonName = signal("")

  constructor(private pokeService: PokeService){
    const pokeData = this.pokeService.getCachedPokemon();
    this.pokemon = signal(pokeData);
    this.subscription = this.pokeService.getPokemonData().subscribe(
      data => {this.pokemon.set(data)}
    );
  }

  getPokemon(input: string){
    this.pokeService.searchPokemon(input);
  }

  ngOnDestroy(){
    this.subscription.unsubscribe();
  }

  getSprites(){
    const sprites = this.pokemon().sprites;
    // Define the exact keys from the interface
    const spriteKeys: (keyof typeof sprites)[] = [
      'back_default',
      'back_shiny',
      'front_default',
      'front_shiny'
    ];
    // Map over the keys to get the corresponding URLs
    return spriteKeys.map(key => sprites[key])
  }

  getTypes(){
    const types = this.pokemon().types;
    return types;
  }

  getMoves(){
    const moves = this.pokemon().moves;
    return moves;
  }

}
