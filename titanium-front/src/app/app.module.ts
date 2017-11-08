import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ApiRestService } from './api-rest/api-rest.service';
import { ElementViewComponent } from './element-view/element-view.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material-module/material.module';
import { SearchComponent } from './search/search.component';
import { InventoryComponent } from './inventory/inventory.component';
import { ElementListComponent } from './element-list/element-list.component';
import {HttpClient, HttpClientModule, HttpHandler} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { ToOrderComponent } from './to-order/to-order.component';
import { ByLocationComponent } from './by-location/by-location.component';
import {LocationService} from './location/location.service';
import { LocationPipe } from './location/location.pipe';
import { InventoryElementFormComponent } from './inventory-element-form/inventory-element-form.component';




@NgModule({
  declarations: [
    AppComponent,
    ElementViewComponent,
    SearchComponent,
    InventoryComponent,
    ElementListComponent,
    ToOrderComponent,
    ByLocationComponent,
    LocationPipe,
    InventoryElementFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [ApiRestService, LocationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
