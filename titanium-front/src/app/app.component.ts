import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {OverlayContainer} from '@angular/cdk/overlay';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Titanium';

  dark = false;

  currentFocus = null;

  searchFocus = {title: 'Rechercher'};
  inventoryFocus = {title: 'Inventaire'};
  toOrderFocus = {title: 'A commander'};
  byLocationFocus = {title: 'Par étagère'};

  themes = [{className: 'unicorn-dark-theme', name: 'Unicorn dark'},
    {className: 'deeppurple-amber-theme', name: 'Deep purple - amber'},
    {className: 'indigo-pink-theme', name: 'Indigo - pink'}];

  currentTheme = 'indigo-pink-theme';

  constructor(private _element: ElementRef,
              private _renderer: Renderer2,
              private _overlayContainer: OverlayContainer) {
  }

  ngOnInit(): void {
    this.currentFocus = this.searchFocus;
  }

  onMenuClick(newFocus) {
    this.currentFocus = newFocus;
  }

  switchTheme(theme: string) {
    this._renderer.removeClass(this._element.nativeElement, this.currentTheme);
    this._overlayContainer.getContainerElement().classList.remove(this.currentTheme);

    this.currentTheme = theme;

    this._renderer.addClass(this._element.nativeElement, this.currentTheme);
    this._overlayContainer.getContainerElement().classList.add(this.currentTheme);
  }
}
