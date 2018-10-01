import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {ApiRestService} from "../api-rest/api-rest.service";

@Component({
  selector: 'app-reset-all-elements-dialog',
  templateUrl: './reset-all-elements-dialog.component.html',
  styleUrls: ['./reset-all-elements-dialog.component.css']
})
export class ResetAllElementsDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ResetAllElementsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private apiRestService: ApiRestService) {
  }

  ngOnInit() {
  }

  resetAllElements() {
    this.apiRestService.resetAllElements().subscribe();
    this.dialogRef.close();
  }

}
