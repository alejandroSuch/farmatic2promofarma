import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output, ViewEncapsulation} from "@angular/core";
import {Product} from "../../domain/Product";


@Component({
  selector: 'fa-checkbox',
  templateUrl: './checkbox.component.html',
  styleUrls: ['./checkbox.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.Native
})
export class CheckboxComponent {
  @Input()
  product: Product = Product.empty();

  @Output()
  change: EventEmitter<any> = new EventEmitter<any>();

  constructor() {
  }

  onChange(event) {
    event.preventDefault();
    event.stopPropagation();
  }

  check() {
    const copy = this.product.clone();

    if (this.product.isNotValid()) {
      return;
    }

    copy.revision = !this.product.revision;
    this.change.emit(copy);
  }

}
