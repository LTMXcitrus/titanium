import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {Batch} from '../model/batch';
import {InventoryElement} from '../model/inventory-element';


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

  getInventoryByShelf(): Observable<Map<string, InventoryElement[]>> {
    return this.http.get<Map<string, InventoryElement[]>>(this.remoteRootUrl + 'inventory/byShelf');
  }

}
