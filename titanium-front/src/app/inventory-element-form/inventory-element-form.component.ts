import {Component, Input, OnInit} from '@angular/core';
import {InventoryElement} from '../model/inventory-element';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-inventory-element-form',
  templateUrl: './inventory-element-form.component.html',
  styleUrls: ['./inventory-element-form.component.css']
})
export class InventoryElementFormComponent implements OnInit {

  @Input()
  inventoryElements: InventoryElement[];

  formGroups: any[] = [];

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.inventoryElements.forEach((inventoryElement) => {
      this.formGroups.push({element: inventoryElement, formGroup: this.formBuilder.group(
        {
          quantity: ['', Validators.required],
          expirationDate: ['', Validators.required]
        })});
    });
  }

}
