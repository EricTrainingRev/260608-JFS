import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Monolith } from "./components/monolith/monolith";
import { Heading } from "./components/heading/heading";
import { Instructions } from "./components/instructions/instructions";
import { Search } from "./components/search/search";
import { Pokename } from "./components/pokename/pokename";
import { PokeService } from './services/pokeservice';

/**
 * Root component of the application.
 * Sets up the layout and routing outlet for rendering child views.
 * Includes static components like heading and search, and uses signals for reactive state.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Monolith, Heading, Instructions, Search, Pokename],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  /**
   * Reactive signal holding the application title.
   * Can be used for dynamic updates or metadata binding.
   */
  protected readonly title = signal('pokedex');

  constructor(private router: Router, private pokeService: PokeService) {}
}
