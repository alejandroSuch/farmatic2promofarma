import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCheckboxModule} from '@angular/material/checkbox';
import {NgxDatatableModule} from "@swimlane/ngx-datatable";

const materialModules = [
  MatToolbarModule,
  MatCheckboxModule,
  NgxDatatableModule
];

@NgModule({
  imports: materialModules,
  exports: materialModules
})
export class MaterialModule {

}
