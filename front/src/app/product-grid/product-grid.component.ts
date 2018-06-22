import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Product} from "../domain/Product";
import {PageEvent} from "@angular/material";

@Component({
  selector: 'fa-product-grid',
  templateUrl: './product-grid.component.html',
  styleUrls: ['./product-grid.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductGridComponent implements OnChanges {
  @Input()
  products: Product[];

  productsToShow: Product[] = [];

  pageIndex: number = 0;

  pageSize: number = 10;

  @Output()
  onProductChanged: EventEmitter<Product> = new EventEmitter<Product>();

  editing: string = null;

  displayedColumns = ['name', 'uniqueCode', 'ean', 'cn', 'revision'];


  ngOnChanges(changes: SimpleChanges): void {
    if(changes['products'] && changes['products'].currentValue instanceof Array) {
      this.products = changes['products'].currentValue;

      this.onPage({
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        length: changes['products'].currentValue.length,
        previousPageIndex: 0
      });
    }
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
      const copy = product.clone();
      copy.uniqueCode = uniqueCode;
      this.onProductChanged.emit(copy);
    }

    this.editing = null;
  }

  check(product: Product) {
    const productToEmit = product.clone();
    productToEmit.revision = !product.revision;
    this.onProductChanged.emit(productToEmit);
  }

  onPage(event:PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;

    this.productsToShow = this.products.slice(this.pageIndex, this.pageIndex + this.pageSize);
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
