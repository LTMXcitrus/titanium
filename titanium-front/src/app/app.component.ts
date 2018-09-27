import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {OverlayContainer} from '@angular/cdk/overlay';
import {Router, RoutesRecognized} from '@angular/router';





@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Titanium';

  themes = [{className: 'unicorn-dark-theme', name: 'Unicorn dark'},
    {className: 'deeppurple-amber-theme', name: 'Deep purple - amber'},
    {className: 'indigo-pink-theme', name: 'Indigo - pink'},
    {className: 'pink-bluegrey-theme', name: 'Pink - blue-grey'},
    {className: 'purple-green-theme', name: 'Purple - green'}];

  currentTheme = 'indigo-pink-theme';
  subtitle = 'Rechercher';

  constructor(private _element: ElementRef,
              private _renderer: Renderer2,
              private _overlayContainer: OverlayContainer,
              private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe((data) => {
      if (data instanceof RoutesRecognized) {
        this.subtitle = data.state.root.firstChild.data.title;
      }
    });
  }

  switchTheme(theme: string) {
    this._renderer.removeClass(this._element.nativeElement, this.currentTheme);
    this._overlayContainer.getContainerElement().classList.remove(this.currentTheme);

    this.currentTheme = theme;

    this._renderer.addClass(this._element.nativeElement, this.currentTheme);
    this._overlayContainer.getContainerElement().classList.add(this.currentTheme);
  }
}
