import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {Batch} from '../model/batch';
import {InventoryElement} from '../model/inventory-element';
import {DriveFile} from '../model/drive-file';


@Injectable()
export class ApiRestService {
  private remoteRootUrl = 'http://localhost:8080/rest/';

  constructor(private http: HttpClient) {
  }

  getAllElements(): Observable<Element[]> {
    return this.http.get<Element[]>(this.remoteRootUrl + '/elements/');
  }

  getElementsFromBatch(batch: Batch): Observable<Element[]> {
    return this.http.get<Element[]>(this.remoteRootUrl + '/elements/batch/' + batch);
  }

  getElementsToOrder() {
    return this.http.get<Element[]>(this.remoteRootUrl + 'elements/toOrder');
  }

  searchElements(query: string): Observable<Element[]> {
    return this.http.get<Element[]>(this.remoteRootUrl + 'elements/search/' + query);
  }

  getInventory(): Observable<InventoryElement[]> {
    return this.http.get<InventoryElement[]>(this.remoteRootUrl + 'inventory/start');
  }

  getInventoryByShelf(shelf: string): Observable<InventoryElement[]> {
    return this.http.get<InventoryElement[]>(this.remoteRootUrl + 'inventory/byShelf/' + shelf);
  }

  savePartialInventory(partialInventory: InventoryElement[]): Observable<InventoryElement[]> {
    return this.http.post<InventoryElement[]>(this.remoteRootUrl + 'inventory/partial', partialInventory);
  }

  getFilesFromFolder(folder): Observable<DriveFile[]> {
    return this.http.get<DriveFile[]>(this.remoteRootUrl + 'drive/folder/' + folder + '/files');
  }

  importDataFromFolder(file: DriveFile): Observable<any> {
    console.log(JSON.stringify(file));
    return this.http.get<any>(this.remoteRootUrl + 'sheets/import/folder/' + file.fileId);
  }

}
