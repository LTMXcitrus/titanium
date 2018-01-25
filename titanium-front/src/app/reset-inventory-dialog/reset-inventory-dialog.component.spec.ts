import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetInventoryDialogComponent } from './reset-inventory-dialog.component';

describe('ResetInventoryDialogComponent', () => {
  let component: ResetInventoryDialogComponent;
  let fixture: ComponentFixture<ResetInventoryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetInventoryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetInventoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
