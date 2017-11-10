import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {ApiRestService} from '../api-rest/api-rest.service';
import {DriveFile} from '../model/drive-file';

@Component({
  selector: 'app-import-export-dialog',
  templateUrl: './import-export-dialog.component.html',
  styleUrls: ['./import-export-dialog.component.css']
})
export class ImportExportDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ImportExportDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }


  ngOnInit() {

  }

  chosenImportFile(file: DriveFile) {
    this.dialogRef.close({type: 'import', file: file});
  }

}
