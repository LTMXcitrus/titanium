import {Component, Input, OnInit} from '@angular/core';
import {ClosetLocation} from '../model/closet-location';
import {DataSource} from '@angular/cdk/typings/collections';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-element-view',
  templateUrl: './element-view.component.html',
  styleUrls: ['./element-view.component.css']
})
export class ElementViewComponent implements OnInit {

  @Input()
  element: Element;

  @Input()
  filter: string;



  elementAsList: Element[];

  constructor() {
  }

  ngOnInit() {
    this.elementAsList = [this.element];
  }


}
