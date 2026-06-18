import { Injectable } from '@angular/core';

/*
  In Angular what we call services are classes decorated with the @Injectable
  decorator. This tells Angular that the class is a resource that we expect
  to utilize in other parts of our applicaiton. Typical use cases are activities
  like shared state management and shared functionality.

  Beyond the examples above, any sort of business logic should be handle by
  one or more services, not the components directly. Use Components to build
  the visual aspects of your application (and do a slight bit of data management
  and/or signaling between them) but leave the heavy logic and actions of your
  application to your services

  By default all services you utilize we be singletons: you can configure a
  service to instead work like a Spring prototype bean where each place it is
  referenced gets a new version of the service, but this is not common
*/
@Injectable({
  providedIn: 'root',
})
export class ExampleService {
  private myServiceField = "Some private data"

  getMyServiceFieldData(){
    return this.myServiceField;
  }
}
