import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ApiRestService } from './api-rest/api-rest.service';
import { ElementViewComponent } from './element-view/element-view.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MaterialModule} from "./material-module/material.module";
import { SearchComponent } from './search/search.component';
import { InventoryComponent } from './inventory/inventory.component';
import { ElementListComponent } from './element-list/element-list.component';
import {HttpClient, HttpClientModule, HttpHandler} from "@angular/common/http";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    AppComponent,
    ElementViewComponent,
    SearchComponent,
    InventoryComponent,
    ElementListComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [ApiRestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
