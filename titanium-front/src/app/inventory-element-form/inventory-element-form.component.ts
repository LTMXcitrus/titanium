import {Component, Input, OnInit} from '@angular/core';
import {InventoryElement} from '../model/inventory-element';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ApiRestService} from '../api-rest/api-rest.service';

@Component({
  selector: 'app-inventory-element-form',
  templateUrl: './inventory-element-form.component.html',
  styleUrls: ['./inventory-element-form.component.css']
})
export class InventoryElementFormComponent implements OnInit {

  @Input()
  elements: InventoryElement[];

  @Input()
  location: any;

  inventoryElements: InventoryElement[];

  formGroups: any[] = [];


  constructor(private apiRestService: ApiRestService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    if (this.elements) {
      this.elements.forEach((element) => {
          this.formGroups.push(this.buildElementForm(element));
        }
      );
    }
  }

  save() {
    if (this.isValid()) {
      this.apiRestService.savePartialInventory(this.elements).subscribe(
        response => {
          this.elements = response;
        }
      );
    } else {
      console.log('not valid');
      // todo display snackBar to notify user
    }
  }

  isValid(): boolean {
    return this.formGroups.every((object) => object.formGroup.status === 'VALID');
  }

  buildElementForm(element): any {
    if (element.perishable) {
      return {
        element: element, formGroup:
          new FormGroup({
            quantity: new FormControl('', [Validators.required]),
            expirationDate: new FormControl('', [Validators.required,
              Validators.pattern('(20[0-9]{2})-((1[0-2])|(0[1-9]))-(((1|2)[0-9])|(0[1-9])|3(0|1))')])
          })
      };
    } else {
      return {
        element: element, formGroup: new FormGroup({
          quantity: new FormControl('', [Validators.required]),
          expirationDate: new FormControl('', [Validators.pattern('(20[0-9]{2})-((1[0-2])|(0[1-9]))-(((1|2)[0-9])|(0[1-9])|3(0|1))')])
        })
      };
    }
  }

}

// ((1[0-2])|(0[1-9]))
