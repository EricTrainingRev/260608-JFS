import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  // the app prefix comes from angular.json
  // we can use this selector to render the component in our app component
  selector: 'app-second-component',
  imports: [],
  templateUrl: './second-component.html',
  styleUrl: './second-component.css',
})
export class SecondComponent {

  /*
    Anytime you need to pass data down from a parent element into a 
    child element you can use the @Input decorator to specify to Angular
    that the value of the field is going to be provided by another resource
  */

  @Input() // by itself this does nothing: we need to provide an input in the parent component
  message = "";

  /*
    If you ever need to signal or pass data to the parent you can use an
    EventEmitter: these resources allow you to perform event binding on your
    child component's custom events and then respond to them in kind
  */

  @Output() // this is our event emitter we can use to signal out
  childEventEmitter = new EventEmitter()

  @Output()
  childEventEmitterWithData: EventEmitter<string> = new EventEmitter();

  triggerChildEvent(){
    // to trigger a signal emission use the emit function: if you need to pass
    // any data to the receiver of the event you can pass it as an argument of
    // emit
    this.childEventEmitter.emit();
  }

  triggerSendingOfStringData(){
    this.childEventEmitterWithData.emit(this.message);
  }

}
