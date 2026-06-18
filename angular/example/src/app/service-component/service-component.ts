import { Component, inject } from '@angular/core';
import { ExampleService } from '../service/example-service';

@Component({
  selector: 'app-service-component',
  imports: [],
  templateUrl: './service-component.html',
  styleUrl: './service-component.css',
})
export class ServiceComponent {

  /*
    To inject a service into a component for use you have two options:
    1. use the inject() function
    2. use constructor injection
  */

  // this is the recommended way to inject services across all Angular resources
  private myService = inject(ExampleService)

  // this also works, but keep in mind there is a shift in Angular development to favoring the inject function
  constructor(private myConstructorInjectedService: ExampleService){}

}
