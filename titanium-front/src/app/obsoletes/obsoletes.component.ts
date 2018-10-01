import { Component, OnInit } from '@angular/core';
import {ApiRestService} from "../api-rest/api-rest.service";
import {Element} from "../model/element";

@Component({
  selector: 'app-obsoletes',
  templateUrl: './obsoletes.component.html',
  styleUrls: ['./obsoletes.component.css']
})
export class ObsoletesComponent implements OnInit {

  obsoletes: Element[];

  constructor(private apiRestService: ApiRestService) { }

  ngOnInit() {
    this.apiRestService.getObsoletesElements().subscribe(
      (response: Element[]) => {
        this.obsoletes = response;
      },
      error => {
        console.log(JSON.stringify(error));
      }
    );
  }

}
