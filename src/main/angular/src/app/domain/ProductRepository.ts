import {Product} from "./Product";

import {Observable} from 'rxjs';
import {Page} from "./Page";

export interface ProductRepository {
  findAll(): Observable<Product[]>;
}
