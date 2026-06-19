import { Component } from '@angular/core';
import { CustomDirective } from '../directive/custom-directive';
import { FormsModule } from '@angular/forms';
import { NgClass, NgStyle } from '@angular/common';

@Component({
  selector: 'app-directive-component',
  imports: [CustomDirective, FormsModule, NgClass, NgStyle],
  templateUrl: './directive-component.html',
  styleUrl: './directive-component.css',
})
export class DirectiveComponent {

  percentage = 10;

  setColorClass() {
    if(this.percentage <= 30) return 'small'
    else if (this.percentage <=70) return 'medium'
    else return 'large'
  }

}
