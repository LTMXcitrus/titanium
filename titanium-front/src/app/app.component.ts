import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Titanium';

  currentFocus = null;

  searchFocus = 0;
  inventoryFocus = 1;

  ngOnInit(): void {
    this.currentFocus = this.searchFocus;
  }

  onMenuClick(newFocus){
    this.currentFocus = newFocus;
  }
}
