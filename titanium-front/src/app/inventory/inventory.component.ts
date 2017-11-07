import {Component, OnInit} from '@angular/core';
import {ApiRestService} from '../api-rest/api-rest.service';
import {InventoryElement} from '../model/inventory-element';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LocationService} from '../location/location.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {

  inventoryElements: Map<string, InventoryElement[]>;

  shelvesFormGroup: any[] = [];

  constructor(private apiRestService: ApiRestService,
              private _formBuilder: FormBuilder,
              private locationService: LocationService) {
  }

  ngOnInit() {
    this.apiRestService.getInventoryByShelf().subscribe(
      response => {
        this.inventoryElements = response;
        this.buildForms();
      },
      error => {

      }
    );
  }

  buildForms() {
    this.locationService.getLocations().forEach((location) => {
      console.log('shelvesFormGroup: ' + this);
      this.shelvesFormGroup.push({location: location, formGroup: this._formBuilder.group({})});
    });
  }

}
