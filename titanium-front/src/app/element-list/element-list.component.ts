import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-element-list',
  templateUrl: './element-list.component.html',
  styleUrls: ['./element-list.component.css']
})
export class ElementListComponent implements OnInit {

  @Input()
  elements: Element[];

  @Input()
  filter: string;

  constructor() { }

  ngOnInit() {
  }

}
