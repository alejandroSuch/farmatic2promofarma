import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCheckboxModule} from '@angular/material/checkbox';
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {MatSnackBarModule} from '@angular/material/snack-bar';

const materialModules = [
  MatToolbarModule,
  MatCheckboxModule,
  NgxDatatableModule,
  MatSnackBarModule
];

@NgModule({
  imports: materialModules,
  exports: materialModules
})
export class MaterialModule {

}
