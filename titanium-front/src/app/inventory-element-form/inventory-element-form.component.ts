import {Component, Input, OnInit} from '@angular/core';
import {InventoryElement} from '../model/inventory-element';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-inventory-element-form',
  templateUrl: './inventory-element-form.component.html',
  styleUrls: ['./inventory-element-form.component.css']
})
export class InventoryElementFormComponent implements OnInit {

  @Input()
  location: any;

  inventoryElements: InventoryElement[];

  formGroups: any[] = [];


  constructor(private apiRestService: ApiRestService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.apiRestService.getInventoryByShelf(this.location.name).subscribe(
      response => {
        this.inventoryElements = response;
        this.inventoryElements.forEach((inventoryElement) => {
          this.formGroups.push({
            element: inventoryElement, formGroup: this.formBuilder.group(
              {
                quantity: ['', Validators.required],
                expirationDate: ['', Validators.required]
              })
          });
        });
      },
      error => {

      }
    );
  }

  validate() {
    if (this.isValid()) {
      // TODO save current inventory into DB via api
    } else {
      console.log('not valid');
      // todo display snackBar to notify user
    }
  }

  isValid(): boolean {
    return this.formGroups.every((object) => object.formGroup.status === 'VALID');
  }

}
