import {Component, OnInit} from '@angular/core';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  elements: Element[];

  constructor(private apiRestService: ApiRestService) {
  }

  ngOnInit() {
  }


  search(query: string) {
    console.log('searching with query: ' + query);
    if (query.length > 2) {
      this.apiRestService.searchElements(query).subscribe(
        response => {
          this.elements = response;
        },
        error => {
          console.log(JSON.stringify(error));
        }
      );
    }
  }

}
