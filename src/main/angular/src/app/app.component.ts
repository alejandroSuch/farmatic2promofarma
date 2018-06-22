import {Component, OnInit} from '@angular/core';
import {ProductRepository} from "./domain/impl/ProductRepository";
import {BehaviorSubject, from, Observable, throwError} from 'rxjs';
import {Product} from "./domain/Product";
import {catchError, filter, map, switchMap, take, tap, toArray} from "rxjs/operators";
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
      .pipe(take(ONLY_ONE))
      .subscribe(value => this.findAll$.next(value));

    const withFilter = {text: null, code: null, unreviewed: false, withoutCode: false};
    this.load(withFilter);
  }

  load(withFilter: { text: string, code: string, unreviewed: boolean, withoutCode: boolean }) {
    const unreviewed = (product: Product) => (withFilter.unreviewed ? !product.revision && !!product.uniqueCode : true);
    const withoutCode = (product: Product) => (withFilter.withoutCode ? !product.uniqueCode : true);
    const byText = (product: Product) => (withFilter.text !== null && withFilter.text.trim().length > 0 ? product.name.toUpperCase().includes(withFilter.text.toUpperCase()) : true);
    const byCode = (product: Product) => (withFilter.code !== null ? [product.ean, product.cn, product.uniqueCode].some(code => `${code}`.includes(withFilter.code)) : true);

    const applyFilter = it => from(it).pipe(
      filter(unreviewed),
      filter(withoutCode),
      filter(byCode),
      filter(byText),
      toArray(),
      tap(() => {
        console.log('filter applied');
      })
    );

    this.product$ = this.findAll$.pipe(switchMap(applyFilter));
  }

  filterChanged(withFilter) {
    this.load(withFilter);
  }

  productChanged(product: Product) {
    const showSavedSnackBar = () => this.snackBar.open(SAVED_MESSAGE, OK_ACTION, {duration: 1000});
    const showErrorSnackBarAndRevertChanges = (err) => {
      this.snackBar.open(err || ERROR_MESSAGE, CLOSE_ACTION, {duration: 2000});
      return throwError(product);
    };

    return this.productRepository
      .save(product)
      .pipe(
        tap(showSavedSnackBar),
        switchMap((saved: Product) => this.findAll$.pipe(
          take(ONLY_ONE),
          switchMap(all => from(all)),
          map(it => it.ean === product.ean ? saved : it),
          toArray()
        )),
        catchError(showErrorSnackBarAndRevertChanges)
      )
      .subscribe(value => {
        this.findAll$.next(value);
      });
  }
}
