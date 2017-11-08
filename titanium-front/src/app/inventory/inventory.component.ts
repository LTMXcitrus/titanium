import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {

  locations: any[] = [];

  inventory: any = {};

  constructor(private apiRestService: ApiRestService,
              private locationService: LocationService) {
  }

  ngOnInit() {
    this.locations = this.locationService.getLocations();
    this.locations.forEach((location) => {
      this.apiRestService.getInventoryByShelf(location.name).subscribe(
        response => {
          this.inventory[location.name] = response;
        }
      );
    });
  }

  test() {
    console.log(JSON.stringify(this.inventory));
  }
}
