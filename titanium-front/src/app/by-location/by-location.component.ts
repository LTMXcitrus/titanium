import { Component, OnInit } from '@angular/core';
import {LocationService} from '../location/location.service';

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent implements OnInit {

  locations: any[];

  constructor(private locationService: LocationService) { }

  ngOnInit() {
    this.locations = this.locationService.getLocations();
  }

}
