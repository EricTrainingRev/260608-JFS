import { Component } from '@angular/core';
import { PokeSearch } from "../poke-search/poke-search";
import { PokeName } from "../poke-name/poke-name";
import { PokeSprite } from "../poke-sprite/poke-sprite";

@Component({
  selector: 'app-poke-home',
  imports: [PokeSearch, PokeName, PokeSprite],
  templateUrl: './poke-home.html',
  styleUrl: './poke-home.css',
})
export class PokeHome {}
