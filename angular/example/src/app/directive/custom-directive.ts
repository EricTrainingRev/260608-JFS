import { Directive, ElementRef, OnInit, Renderer2 } from '@angular/core';

/*
  Anytime you know you need to share some styling or a transformation of content
  in multiple elements you can use a custom directive to apply the change for
  you instead of needing to inject a service or update the css for all of the
  components. Instead you can simply apply your directive directly to the resource
  that needs it
*/

@Directive({
  selector: '[appCustomDirective]',
})
export class CustomDirective implements OnInit {
  /*
    To access the element you will want a reference to the element, and to
    make the change be set you will need to use a renderer
  */
  constructor(private element: ElementRef, private renderer: Renderer2) {}
  
  /*
    This function will trigger after the element has been initialized
  */
  ngOnInit(): void {
    // I am hard coding it here, you could make this dyanmic
    const newColor = "red";
    /*
      Using the renderer's setStyle function we can change the styling of the
      element this directive is attached to via three arguments:
      1. a reference to the "nativeElement" via the element we recieved in the
         constructor
      2. a string reference to the style property we want to change
      3. the new value of the style we want to set
    */
    this.renderer.setStyle(this.element.nativeElement, 'color', newColor);
  }
  
  


}
