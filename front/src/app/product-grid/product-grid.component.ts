import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {IProduct, Product} from "../domain/Product";
import {Observable} from "rxjs/index";
import {MatCheckboxChange} from "@angular/material";

@Component({
  selector: 'fa-product-grid',
  templateUrl: './product-grid.component.html',
  styleUrls: ['./product-grid.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductGridComponent {
  @Input()
  products: Observable<Product[]>;

  @Output()
  onProductChanged: EventEmitter<Partial<IProduct>> = new EventEmitter<Partial<IProduct>>();

  editing: number = -1;

  constructor() {
  }

  getRowClass(product: Product) {
    return {
      'pending': product.isValid() && !product.revision,
      'valid': product.isValid() && product.revision,
      'invalid': product.isNotValid()
    };
  }

  updateUniqueCodeFor(product: Product, uniqueCode: string) {
    if (product.uniqueCode !== uniqueCode) {
      const {ean, revision} = product;
      this.onProductChanged.emit({ean, uniqueCode, revision});
    }

    this.editing = -1;
  }

  check(event: MatCheckboxChange, product: Product) {
    if (product.isNotValid()) {
      return;
    }

    const {ean, uniqueCode} = product;
    this.onProductChanged.emit({ean, uniqueCode, revision: event.checked});
  }

  productFilterChanged(filter) {
    console.log('productFilterChanged', filter);
  }

  uniqueCodeFilterChanged(filter) {
    console.log('uniqueCodeFilterChanged', filter);
  }

  cnFilterChanged(filter) {
    console.log('cnFilterChanged', filter);
  }

  eanFilterChanged(filter) {
    console.log('eanFilterChanged', filter);
  }

  revisionFilterChanged(filter) {
    console.log('revisionFilterChanged', filter);
  }

}
