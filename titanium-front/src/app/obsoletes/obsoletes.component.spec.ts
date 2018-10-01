import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObsoletesComponent } from './obsoletes.component';

describe('ObsoletesComponent', () => {
  let component: ObsoletesComponent;
  let fixture: ComponentFixture<ObsoletesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObsoletesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObsoletesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
