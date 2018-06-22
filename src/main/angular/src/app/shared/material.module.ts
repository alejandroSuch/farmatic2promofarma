import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatInputModule} from '@angular/material/input';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';

const materialModules = [
  MatToolbarModule,
  MatCheckboxModule,
  MatSnackBarModule,
  MatSlideToggleModule,
  MatInputModule,
  MatTableModule,
  MatPaginatorModule
];

@NgModule({
  imports: materialModules,
  exports: materialModules
})
export class MaterialModule {

}
