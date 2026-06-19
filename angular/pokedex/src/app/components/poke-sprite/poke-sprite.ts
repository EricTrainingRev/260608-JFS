import { Component, inject, Signal, WritableSignal } from '@angular/core';
import { PokeService } from '../../services/poke-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { Pokemon } from '../../interfaces/pokemon';

@Component({
  selector: 'app-poke-sprite',
  imports: [],
  templateUrl: './poke-sprite.html',
  styleUrl: './poke-sprite.css',
})
export class PokeSprite {

  private pokeService = inject(PokeService);

  pokemonData = toSignal(this.pokeService.pokemon$);

  myobj = {
    first:"one",
    sceond:"two"
  }

  getSpriteUrls(){
    /*
      Something to watch out for: if you use toSignal to save your observable data in a signal you MUST perform
      type narrowing to make sure you do not run into compilation errors. Because toSignal can return either
      your data or an undefined resource you must specify in your actions that you want your data treated
      as your data, not as an undefined resource
    */
    const narrowedPokemonData = this.pokemonData() as Pokemon; // here we specify we want to treat the data as a Pokemon object
    const spriteURL = []; // array to store image urls
    // to get access to the keys of the sprite object we can loop through
    // the object keys and return them for use in the template    
    for(const spriteType in narrowedPokemonData.sprites){ 
      // even though we narrowed the type of the pokemon data earlier, we must do so again for the
      // keys of the object
      const narrowedSpriteType = spriteType as "back_default" | "front_default"
      // even though we limit ourselves to the two properties the rest still exist in the data and we want to ignore them
      if (narrowedSpriteType == "back_default" || narrowedSpriteType == 'front_default'){
        // now whenever we add or remove sprite properties they are dynamically removed/included in our collection of image urls
        spriteURL.push(narrowedPokemonData.sprites[narrowedSpriteType])        
      }

    }
    return spriteURL;
  }

}
