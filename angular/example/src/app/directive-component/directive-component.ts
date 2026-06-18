import { Component } from '@angular/core';
import { NgClass, NgStyle } from "../../../node_modules/@angular/common/types/_common_module-chunk";
import { CustomDirective } from '../directive/custom-directive';

@Component({
  selector: 'app-directive-component',
  imports: [CustomDirective],
  templateUrl: './directive-component.html',
  styleUrl: './directive-component.css',
})
export class DirectiveComponent {

  shouldClassBeActive = true;

}
