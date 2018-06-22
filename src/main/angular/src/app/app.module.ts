import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {MaterialModule} from "./shared/material.module";
import {HttpClientModule} from "@angular/common/http";
import {ProductGridComponent} from './product-grid/product-grid.component';
import {ProductRepository} from "./domain/impl/ProductRepository";
import {environment} from "../environments/environment";
import {API_URL} from "./shared/providers";
import {DebouncedInputDirective} from "./shared/debouncedInput.directive";
import {AutofocusDirective} from "./product-grid/autofocus.directive";
import {CheckboxComponent} from "./product-grid/checkbox/checkbox.component"

@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    ProductGridComponent,
    DebouncedInputDirective,
    AutofocusDirective,
    CheckboxComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: API_URL,
      useValue: environment.apiUrl
    },
    ProductRepository
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
