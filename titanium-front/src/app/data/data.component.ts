import {Component, OnInit} from '@angular/core';
import {MatDialog, MatSnackBar} from '@angular/material';
import {ImportExportDialogComponent} from '../import-export-dialog/import-export-dialog.component';
import {ApiRestService} from '../api-rest/api-rest.service';
import {ProgressDialogComponent} from '../progress-dialog/progress-dialog.component';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css']
})
export class DataComponent implements OnInit {


  constructor(private apiRestService: ApiRestService,
              public dialog: MatDialog,
              public snackBar: MatSnackBar) {
  }

  ngOnInit() {
  }

  openImportDialog(): void {
    const dialogRef = this.dialog.open(ImportExportDialogComponent, {data: {message: '', type: 'import'}});

    dialogRef.afterClosed().subscribe(result => {
      if (result.type === 'import') {
        console.log(JSON.stringify(result));
          this.apiRestService.importDataFromFolder(result.file).subscribe(
            response => {
              this.snackBar.open('Données importées !', null, {duration: 2000});
            }
          );
      }
    });
  }

  saveData() {
    const dialogRef = this.dialog.open(ProgressDialogComponent, {});
    this.apiRestService.saveData().subscribe(
      response => {
        dialogRef.close();
        this.snackBar.open('Données sauvegardées !', null, {duration: 2000});
      }
    );
  }


}
