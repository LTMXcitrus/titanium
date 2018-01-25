import {Component, OnInit} from '@angular/core';
import {LocationService} from '../location/location.service';
import {ApiRestService} from '../api-rest/api-rest.service';
import {MatDialog, MatSnackBar} from '@angular/material';
import {ProgressDialogComponent} from '../progress-dialog/progress-dialog.component';
import {InventoryElement} from '../model/inventory-element';
import {Router} from '@angular/router';

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
              private snackBar: MatSnackBar,
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

  isDone(shelf): boolean {
    return !this.inventory[shelf].some((element: InventoryElement) => !element.uptodate);
  }

  refreshStates(): void {
    this.locations.forEach((location) => {
      this.shelfStates[location.name] = this.isDone(location.name);
    });
  }

  continueInventory(): number {
    if (this.selectedIndex < this.locations.length && this.shelfStates[this.locations[this.selectedIndex].name]) {
      this.selectedIndex ++;
      return this.continueInventory();
    } else {
      return this.selectedIndex;
    }
  }

  inventoryIsDone(): boolean {
    this.refreshStates();
    return this.locations.every((location) => this.shelfStates[location.name]);
  }

  validateInventory() {
    if (this.inventoryIsDone()) {
      this.apiRestService.saveInventory(this.inventory).subscribe(
        response => {
          this.router.navigate(['/byLocation']);
        }
      );
    } else {
      this.snackBar.open('L\'inventaire n\'est pas terminé !');
    }
  }
}
