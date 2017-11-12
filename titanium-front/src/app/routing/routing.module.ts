import {NgModule} from '@angular/core';
import {DataComponent} from '../data/data.component';
import {ByLocationComponent} from '../by-location/by-location.component';
import {ToOrderComponent} from '../to-order/to-order.component';
import {InventoryComponent} from '../inventory/inventory.component';
import {SearchComponent} from '../search/search.component';
import {RouterModule, Routes} from '@angular/router';

const appRoutes: Routes = [
  {path: '', component: SearchComponent, data: {title: 'Rechercher'}},
  {path: 'inventory', component: InventoryComponent, data: {title: 'Inventaire'} },
  {path: 'toOrder', component: ToOrderComponent, data: {title: 'A commander'}},
  {path: 'byLocation', component: ByLocationComponent, data: {title: 'Par étagère'}},
  {path: 'data', component: DataComponent, data: {title: 'Gérer les données'}}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [RouterModule]
})
export class RoutingModule { }
