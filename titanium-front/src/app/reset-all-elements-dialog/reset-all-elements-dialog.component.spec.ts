import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetAllElementsDialogComponent } from './reset-all-elements-dialog.component';

describe('ResetAllElementsDialogComponent', () => {
  let component: ResetAllElementsDialogComponent;
  let fixture: ComponentFixture<ResetAllElementsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetAllElementsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetAllElementsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
