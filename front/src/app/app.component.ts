import {Component, OnInit} from '@angular/core';
import {ProductRepository} from "./domain/impl/ProductRepository";
import {BehaviorSubject, of, from, Observable} from 'rxjs';
import {IProduct, Product} from "./domain/Product";
import {mergeMap, switchMap, take, toArray, map} from "rxjs/operators";

@Component({
  selector: 'fa-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  findAll$: BehaviorSubject<Product[]> = new BehaviorSubject([]);
  product$: Observable<Product[]>;

  constructor(private productRepository: ProductRepository) {

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

  productChanged({id, uniqueCode, revision}: Partial<IProduct>) {
    this.findAll$.pipe(
      take(1),
      mergeMap(it => from(it)),
      switchMap((p: Product) => {
        if (p.id !== id) {
          return of(p);
        }

        const result = p.clone();
        result.uniqueCode = uniqueCode;
        result.revision = !uniqueCode.trim() ? false : revision;

        // TODO: llamada a backend (
        return of(result);
      }),
      toArray()
    )
      .subscribe(data => this.findAll$.next(data));
  }
}
