import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryElementFormComponent } from './inventory-element-form.component';

describe('InventoryElementFormComponent', () => {
  let component: InventoryElementFormComponent;
  let fixture: ComponentFixture<InventoryElementFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InventoryElementFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InventoryElementFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
