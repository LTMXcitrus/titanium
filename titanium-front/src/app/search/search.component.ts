import { Component, OnInit } from '@angular/core';
import {ApiRestService} from "../api-rest/api-rest.service";
import {ClosetLocation} from "../model/closet-location";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(private apiRestService:ApiRestService) { }

  ngOnInit() {
  }

  elements:Element[];
  selectedFilter:string;

  filters = [
    "Etagère",
    "Quantité",
    "Quantité à commander"
  ];

  search(query: string){
    console.log("searching with query: " + query);
    if(query.length > 2) {
      this.apiRestService.searchElements(query).subscribe(
        response => {
          console.log(JSON.stringify(response));
          this.elements = response;
        },
        error => {
          console.log(JSON.stringify(error))
        }
      )
    }
  }

}
