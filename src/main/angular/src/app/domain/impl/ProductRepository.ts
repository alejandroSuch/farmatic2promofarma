import {ProductRepository as IProductRepository} from "../ProductRepository";
import {Inject, Injectable} from "@angular/core";
import {from, Observable, throwError} from "rxjs";
import {IProduct, Product} from "../Product";
import {HttpClient, HttpParams} from "@angular/common/http";

import {map, toArray} from 'rxjs/operators'
import {API_URL} from "../../shared/providers";
import {switchMap} from "rxjs/internal/operators";

const INVALID_FORMAT_MESSAGE = 'Formato incorrecto';

@Injectable()
export class ProductRepository implements IProductRepository {
  private baseUrl: string;

  constructor(private httpClient: HttpClient, @Inject(API_URL)apiUrl: string) {
    this.baseUrl = `${apiUrl}products`;
    console.info('Base url is', this.baseUrl);
  }

  save(product: Product): Observable<Product> {
    if (`${product.uniqueCode}`.trim().length > 0 && !/\d+/.test(product.uniqueCode)) {
      return throwError(INVALID_FORMAT_MESSAGE);
    }

    if (`${product.uniqueCode}`.trim().length === 0) {
      product.revision = false;
    }

    return this.httpClient
      .post(this.baseUrl, Object.assign({}, product, {revision: product.revision ? 1 : 0}))
      .pipe(
        map(() => product)
      )
  }

  findAll(page: number = 0, size: number = 20): Observable<Product[]> {
    return this.httpClient
      .get(this.baseUrl, ProductRepository.findAllOptions(page, size))
      .pipe(
        switchMap((it: IProduct[]) => from(it)),
        map(it => new Product(it)),
        toArray()
      )
  }

  private static findAllOptions(page: number, size: number) {
    const params: HttpParams = new HttpParams().set('page', `${page}`).set('size', `${size}`);
    return {params};
  }
}
