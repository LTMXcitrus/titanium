import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Titanium';

  currentFocus = null;

  searchFocus = {title: 'Rechercher'};
  inventoryFocus = {title: 'Inventaire'};
  toOrderFocus = {title: 'A commander'};
  byLocationFocus = {title: 'Par étagère'};

  ngOnInit(): void {
    this.currentFocus = this.searchFocus;
  }

  onMenuClick(newFocus) {
    this.currentFocus = newFocus;
  }
}
