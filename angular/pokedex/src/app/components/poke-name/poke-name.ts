import { Component, inject } from '@angular/core';
import { PokeService } from '../../services/poke-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-poke-name',
  imports: [AsyncPipe],
  templateUrl: './poke-name.html',
  styleUrl: './poke-name.css',
})
export class PokeName {

  pokeService = inject(PokeService);

}
