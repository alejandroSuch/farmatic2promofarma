import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";

const materialModules = [
  MatToolbarModule
];

@NgModule({
  imports: materialModules,
  exports: materialModules
})
export class MaterialModule {

}
