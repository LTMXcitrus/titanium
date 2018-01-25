import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {ApiRestService} from "../api-rest/api-rest.service";

@Component({
  selector: 'app-reset-inventory-dialog',
  templateUrl: './reset-inventory-dialog.component.html',
  styleUrls: ['./reset-inventory-dialog.component.css']
})
export class ResetInventoryDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ResetInventoryDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private apiRestService: ApiRestService) {
  }

  ngOnInit() {
  }

  resetInventory() {
    this.apiRestService.resetInventory().subscribe();
    this.dialogRef.close()
  }

}
