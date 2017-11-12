import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent implements OnInit {

  locations: any[];

  elements: any = {};

  constructor(private locationService: LocationService,
              private apiRestService: ApiRestService) {
  }

  ngOnInit() {
    this.locations = this.locationService.getLocations();
    this.apiRestService.getElementsByShelf().subscribe(
      response => {
        this.elements = response;
      }
    );
  }

}
