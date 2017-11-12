import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';
import {ApiRestService} from '../api-rest/api-rest.service';
import {MatDialog} from '@angular/material';
import {ProgressDialogComponent} from '../progress-dialog/progress-dialog.component';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {

  locations: any[] = [];

  inventory: any = {};

  constructor(private dialog: MatDialog,
              private apiRestService: ApiRestService,
              private locationService: LocationService) {
  }

  ngOnInit() {
    const dialogRef = this.dialog.open(ProgressDialogComponent, {});
    this.locations = this.locationService.getLocations();
    this.apiRestService.getInventoryByShelf().subscribe(
      response => {
        this.inventory = response;
        dialogRef.close();
      }
    );
  }

  test() {
    console.log(JSON.stringify(this.inventory));
  }
}
