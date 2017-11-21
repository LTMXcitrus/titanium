import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';
import {ApiRestService} from '../api-rest/api-rest.service';
import {MatDialog} from '@angular/material';
import {ProgressDialogComponent} from '../progress-dialog/progress-dialog.component';
import {InventoryElement} from '../model/inventory-element';
import {Router} from "@angular/router";

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {

  locations: any[] = [];

  inventory: any = {};

  shelfStates: any = {};
  selectedIndex = 0;

  constructor(private dialog: MatDialog,
              private apiRestService: ApiRestService,
              private locationService: LocationService,
              private router: Router) {
  }

  ngOnInit() {
    const dialogRef = this.dialog.open(ProgressDialogComponent, {});
    this.locations = this.locationService.getLocations();
    this.apiRestService.getInventoryByShelf().subscribe(
      response => {
        this.inventory = response;
        this.refreshStates();
        this.continueInventory();
        dialogRef.close();
      }
    );
  }

  isDone(shelf) {
    return !this.inventory[shelf].some((element: InventoryElement) => !element.uptodate);
  }

  refreshStates() {
    this.locations.forEach((location) => {
      this.shelfStates[location.name] = this.isDone(location.name);
    });
  }

  continueInventory(): number {
    if (this.shelfStates[this.locations[this.selectedIndex].name]) {
      this.selectedIndex ++;
      return this.continueInventory();
    } else {
      return this.selectedIndex;
    }
  }

  validateInventory() {
    this.apiRestService.saveInventory(this.inventory).subscribe(
      response => {
        this.router.navigate(['/byLocation']);
      }
    );
  }
}
