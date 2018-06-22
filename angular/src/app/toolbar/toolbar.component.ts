import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'fa-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  filter: any = {
    text: null,
    code: null,
    unreviewed: false,
    withoutCode: false
  };

  @Output()
  filterChange: EventEmitter<any> = new EventEmitter();

  constructor() {
  }

  ngOnInit() {

  }

  toggleWithoutCode(withoutCode) {
    this.filter = Object.assign({}, this.filter, {withoutCode});
    this.emitFilterChange();
  }

  toggleUnreviewed(unreviewed) {
    this.filter = Object.assign({}, this.filter, {unreviewed});
    this.emitFilterChange();
  }

  searchTextChanged(searchText) {
    const isNumber = /\d+/.test(searchText)
    this.filter = Object.assign({}, this.filter, {
      code: (isNumber ? searchText : null),
      text: (isNumber ? null : searchText)
    });

    this.emitFilterChange();
  }

  private emitFilterChange() {
    this.filterChange.emit(this.filter);
  }
}
