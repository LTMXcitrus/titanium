import { Component, OnInit } from '@angular/core';
import {ApiRestService} from "../api-rest/api-rest.service";
import {MatDialog} from "@angular/material";
import {ResetInventoryDialogComponent} from "../reset-inventory-dialog/reset-inventory-dialog.component";
import {ResetAllElementsDialogComponent} from "../reset-all-elements-dialog/reset-all-elements-dialog.component";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  constructor(public dialog: MatDialog) {}

  ngOnInit() {
  }

  openResetInventoryDialog(): void {
    this.dialog.open(ResetInventoryDialogComponent);
  }

  resetAllElements() {
    this.dialog.open(ResetAllElementsDialogComponent);
  }
}
