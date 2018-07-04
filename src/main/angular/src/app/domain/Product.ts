export interface IProduct {
  uniqueCode: string;
  cn: string;
  ean: string;
  name: string;
  revision: number;
}

export class Product {
  public uniqueCode: string;
  public cn: string;
  public ean: string;
  public name: string;
  public revision: boolean;

  static empty(): Product {
    return new Product({
      uniqueCode: null,
      cn: null,
      ean: null,
      name: null,
      revision: 0
    });
  }

  constructor({uniqueCode, cn, ean, name, revision}: IProduct) {
    this.uniqueCode = uniqueCode;
    this.cn = cn;
    this.ean = ean;
    this.name = name;
    this.revision = revision === 1;
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
    const {uniqueCode, cn, ean, name} = this;
    const revision = this.revision ? 1 : 0;
    return new Product({uniqueCode, cn, ean, name, revision});
  }
}
