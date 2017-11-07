import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {

  locations: any[] = [];

  constructor(private locationService: LocationService) {
  }

  ngOnInit() {
    this.locations = this.locationService.getLocations();
  }
}
