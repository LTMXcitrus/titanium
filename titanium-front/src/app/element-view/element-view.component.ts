import {Component, Input, OnInit} from '@angular/core';
import {Element} from '../model/element';
import {MatDialog, MatSnackBar} from '@angular/material';
import {ElementEditComponent} from '../element-edit/element-edit.component';
import {ApiRestService} from "../api-rest/api-rest.service";
import {ProgressDialogComponent} from "../progress-dialog/progress-dialog.component";

@Component({
  selector: 'app-element-view',
  templateUrl: './element-view.component.html',
  styleUrls: ['./element-view.component.css']
})
export class ElementViewComponent implements OnInit {

  @Input()
  element: Element;

  constructor(private dialog: MatDialog,
              private apiRestService: ApiRestService,
              public snackBar: MatSnackBar) {
  }

  ngOnInit() {
  }

  editElement() {
    const editElement = JSON.parse(JSON.stringify(this.element));
    const dialogRef = this.dialog.open(ElementEditComponent, {data: editElement});
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const progressDialog = this.dialog.open(ProgressDialogComponent, {});
        this.element = result;
        this.apiRestService.saveElement(this.element).subscribe(response => {
          progressDialog.close();
          this.snackBar.open(response, null, {duration: 2000});
        });
      }
    });
  }

}
