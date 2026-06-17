import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/*
  Components are the fundamental building blocks of Angular. You don't just
  add HTML elements to your Angular app: you create components, and those
  components are what produce the structure of your application. Your components
  have a few core configurations:
  - selector -> this tells Angular what tags to use to reference your component
                in any HTML template
  - imports -> when we start putting components in other components and adding in
               services we will need to import them so the component knows it
               has access to them
  - templateURL -> this is a reference to the html file that acts as the component
                   HTML template. You can also hardcode your template in the
                   component
  - styleURL -> this is a reference to your external style sheet. Same as the HTML
                template, this can be hard-coded in the component
*/

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  /*
    Ideally you should have this distinction between your HTML template and
    your component class:
    - HTML Template -> this should provide the structure (quite literally the
                       template) of the component
    - Component Class -> this should provide the data for the component. If you
                         want to display it in the HTML it should be defined
                         here
  */
    readonly message = signal("but you should really do this");
}
