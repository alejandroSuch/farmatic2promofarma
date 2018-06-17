export interface Page<T> {
  content: T[];
  totalPages: number;
  last: boolean;
  totalElements: number;
  size: number;
  number: number;
  sort: string;
  first: boolean;
  numberOfElements: number;
}
