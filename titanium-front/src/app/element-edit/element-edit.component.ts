import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatChipInputEvent, MatDialogRef} from '@angular/material';
import {Element} from '../model/element';
import {ENTER} from '@angular/cdk/keycodes';
import {LocationService} from "../location/location.service";
const COMMA = 188;

@Component({
  selector: 'app-element-edit',
  templateUrl: './element-edit.component.html',
  styleUrls: ['./element-edit.component.css']
})
export class ElementEditComponent implements OnInit {

  editElement: Element;

  locations: any[];

  minimumEditable = false;

  separatorKeyCodes = [ENTER, COMMA];

  constructor(public dialogRef: MatDialogRef<ElementEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private locationService: LocationService) {
  }

  ngOnInit() {
    this.editElement = this.data;
    this.locations = this.locationService.getLocations();
  }

  removeTag(tag): void {
    this.editElement.tags.splice(this.editElement.tags.indexOf(tag), 1);
  }

  addTag(event: MatChipInputEvent) {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.editElement.tags.push(value);
    }

    if (input) {
      input.value = '';
    }
  }

}
