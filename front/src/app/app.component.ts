import {Component, OnInit} from '@angular/core';
import {ProductRepository} from "./domain/impl/ProductRepository";
import {BehaviorSubject, from, Observable, of} from 'rxjs';
import {IProduct, Product} from "./domain/Product";
import {catchError, concatMap, switchMap, take, tap, toArray} from "rxjs/operators";
import {MatSnackBar} from "@angular/material";

const ONLY_ONE = 1;
const OK_ACTION = "Ok";
const CLOSE_ACTION = "Cerrar";
const SAVED_MESSAGE = "Guardado";
const ERROR_MESSAGE = "Se ha producido un error y el producto no se ha guardado";

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
      take(ONLY_ONE),
      switchMap(it => from(it)),
      concatMap((product: Product) => {
        if (product.ean !== ean) {
          return of(product);
        }

        const oldUniqueCode = product.uniqueCode;
        const oldRevision = product.revision;

        product.uniqueCode = uniqueCode;
        product.revision = !uniqueCode.trim() ? false : revision;

        const showSavedSnackBar = () => this.snackBar.open(SAVED_MESSAGE, OK_ACTION, {duration: 1000});
        const showErrorSnackBarAndRevertChanges = () => {
          this.snackBar.open(ERROR_MESSAGE, CLOSE_ACTION, {duration: 2000});
          product.uniqueCode = oldUniqueCode;
          product.revision = oldRevision;

          return of(product);
        };

        return this.productRepository
          .save(product)
          .pipe(
            tap(showSavedSnackBar),
            catchError(showErrorSnackBarAndRevertChanges)
          );
      }),
      toArray()
    )
      .subscribe(data => this.findAll$.next(data));
  }
}
