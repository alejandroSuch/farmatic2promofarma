import {ProductRepository as IProductRepository} from "../ProductRepository";
import {Inject, Injectable} from "@angular/core";
import {from, Observable} from "rxjs";
import {IProduct, Product} from "../Product";
import {HttpClient, HttpParams} from "@angular/common/http";

import {map, tap, toArray} from 'rxjs/operators'
import {API_URL} from "../../shared/providers";
import {mergeMap} from "rxjs/internal/operators";

const ONE = 1;

@Injectable()
export class ProductRepository implements IProductRepository {
  private baseUrl: string;

  constructor(private httpClient: HttpClient, @Inject(API_URL)apiUrl: string) {
    this.baseUrl = `${apiUrl}products`;
    console.info('Base url is', this.baseUrl);
  }

  findAll(page: number = 0, size: number = 20): Observable<Product[]> {
    return this.httpClient
      .get(this.baseUrl, ProductRepository.findAllOptions(page, size))
      .pipe(
        mergeMap((it: IProduct[]) => from(it)),
        map(it => new Product(it)),
        toArray()
      )
  }

  private static findAllOptions(page: number, size: number) {
    const params: HttpParams = new HttpParams().set('page', `${page}`).set('size', `${size}`);
    return {params};
  }
}
