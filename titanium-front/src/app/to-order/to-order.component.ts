import {Component, OnInit} from '@angular/core';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-to-order',
  templateUrl: './to-order.component.html',
  styleUrls: ['./to-order.component.css']
})
export class ToOrderComponent implements OnInit {

  elementsToOrder: Element[];

  constructor(private apiRestService: ApiRestService) {
  }

  ngOnInit() {
    this.apiRestService.getElementsToOrder().subscribe(
      response => {
              this.elementsToOrder = response;
      },
      error => {
      }
    );
  }

}
