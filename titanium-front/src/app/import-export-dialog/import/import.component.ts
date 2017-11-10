import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ApiRestService} from '../../api-rest/api-rest.service';
import {DriveFile} from '../../model/drive-file';

@Component({
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.css']
})
export class ImportComponent implements OnInit {

  @Output()
  chosenFile: EventEmitter<DriveFile> = new EventEmitter();

  constructor(private apiRestService: ApiRestService) { }

  files: DriveFile[];

  ngOnInit() {
    this.apiRestService.getFilesFromFolder('titanium').subscribe( response => {
      this.files = response;
    });
  }

  chooseFile(file: DriveFile) {
    this.chosenFile.emit(file);
  }

}
