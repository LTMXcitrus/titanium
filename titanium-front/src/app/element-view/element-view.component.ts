import {Component, Input, OnInit} from '@angular/core';
import {SearchComponent} from "../search/search.component";
import {ClosetLocation} from "../model/closet-location";

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

  closetLocation = ClosetLocation;

  filters = [
    "Etagère",
    "Quantité",
    "Quantité à commander"
  ];

  constructor() { }

  ngOnInit() {

  }

}
