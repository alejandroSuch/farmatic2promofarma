export interface IProduct {
  id: number;
  uniqueCode: string;
  cn: string;
  ean: string;
  name: string;
  revision: boolean;
}

export class Product {
  public id: number;
  public uniqueCode: string;
  public cn: string;
  public ean: string;
  public name: string;
  public revision: boolean;

  constructor({id, uniqueCode, cn, ean, name, revision}: IProduct) {
    this.id = id;
    this.uniqueCode = uniqueCode;
    this.cn = cn;
    this.ean = ean;
    this.name = name;
    this.revision = revision;
  }

  isValid(): boolean {
    if (!this.uniqueCode) {
      return false;
    }

    if (!this.cn) {
      return false;
    }

    return `${this.ean}`.length === 13;
  }

  isNotValid(): boolean {
    return !this.isValid();
  }

  clone() {
    return new Product(this);
  }
}
