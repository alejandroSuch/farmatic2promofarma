import {NgModule} from "@angular/core";
import {TableModule} from 'primeng/table';
import {PaginatorModule} from 'primeng/paginator';


const primeNgModules = [
  TableModule,
  PaginatorModule
];

@NgModule({
  imports: primeNgModules,
  exports: primeNgModules
})
export class PrimengModule{

}
