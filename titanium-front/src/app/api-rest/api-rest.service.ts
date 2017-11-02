import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";


@Injectable()
export class ApiRestService {
  private remoteRootUrl = "http://localhost:8080/rest/";

  constructor(private http: HttpClient) { }

  getAllElements(): Observable<Element[]> {
    return this.http.get<Element[]>(this.remoteRootUrl + "/elements/");
  }

}
