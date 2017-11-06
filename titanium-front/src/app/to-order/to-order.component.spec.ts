import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ToOrderComponent } from './to-order.component';

describe('ToOrderComponent', () => {
  let component: ToOrderComponent;
  let fixture: ComponentFixture<ToOrderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ToOrderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ToOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
