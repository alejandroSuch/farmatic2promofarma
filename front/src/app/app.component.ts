import {Component, OnInit} from '@angular/core';
import {ProductRepository} from "./domain/impl/ProductRepository";
import {BehaviorSubject, from, Observable, of} from 'rxjs';
import {IProduct, Product} from "./domain/Product";
import {concatMap, take, toArray} from "rxjs/operators";
import {catchError, switchMap} from "rxjs/internal/operators";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'fa-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  findAll$: BehaviorSubject<Product[]> = new BehaviorSubject([]);
  product$: Observable<Product[]>;

  constructor(private productRepository: ProductRepository, private snackBar: MatSnackBar) {

  }

  ngOnInit(): void {
    this.productRepository.findAll()
      .pipe(take(1))
      .subscribe(data => this.findAll$.next(data));

    this.load();
  }

  load() {
    this.product$ = this.findAll$;
  }

  productChanged({ean, uniqueCode, revision}: Partial<IProduct>) {
    this.findAll$.pipe(
      take(1),
      switchMap(it => from(it)),
      concatMap((product: Product) => {
        if (product.ean !== ean) {
          return of(product);
        }

        const oldUniqueCode = product.uniqueCode;
        const oldRevision = product.revision;

        product.uniqueCode = uniqueCode;
        product.revision = !uniqueCode.trim() ? false : revision;

        return this.productRepository
          .save(product)
          .pipe(
            catchError(() => {
              this.snackBar.open("Se ha producido un error y el producto no se ha guardado", "Cerrar", {duration: 2000});
              product.uniqueCode = oldUniqueCode;
              product.revision = oldRevision;

              return of(product);
            })
          );
      }),
      toArray()
    )
      .subscribe(data => this.findAll$.next(data));
  }
}
