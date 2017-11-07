import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  MatButtonModule, MatCardModule, MatChipsModule, MatExpansionModule, MatFormFieldModule, MatGridListModule, MatIconModule, MatInputModule,
  MatListModule,
  MatSelectModule,
  MatSidenavModule, MatStepperModule, MatTableModule,
  MatToolbarModule
} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatButtonModule,
    MatExpansionModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatChipsModule,
    MatTableModule,
    MatGridListModule,
    MatStepperModule,
  ],
  exports: [
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatButtonModule,
    MatExpansionModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatChipsModule,
    MatTableModule,
    MatGridListModule,
    MatStepperModule
  ],
  declarations: []
})
export class MaterialModule {
}
