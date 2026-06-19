import { Component, inject } from '@angular/core';
import { PokeService } from '../../services/poke-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-poke-search',
  imports: [FormsModule],
  templateUrl: './poke-search.html',
  styleUrl: './poke-search.css',
})
export class PokeSearch {

  pokemonIdentifier = '';

  private pokeService = inject(PokeService);

  searchForPokemon(){
    this.pokeService.queryPokemon(this.pokemonIdentifier);
  }

}
