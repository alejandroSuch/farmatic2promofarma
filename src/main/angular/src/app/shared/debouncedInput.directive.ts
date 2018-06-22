import {Directive, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output} from "@angular/core";

import {BehaviorSubject, Subject, Subscription} from 'rxjs';
import {debounceTime, filter} from 'rxjs/operators';


const DEFAULT_DELAY = 300;
@Directive({
  selector: '[debouncedInput]'
})
export class DebouncedInputDirective implements OnInit, OnDestroy {
  @Input()
  delay: number = DEFAULT_DELAY;

  @Output()
  debouncedInput: EventEmitter<Event> = new EventEmitter<Event>();

  subscription: Subscription;
  inputSubject: Subject<Event> = new BehaviorSubject<Event>(null);

  constructor() {
  }

  @HostListener('input', ['$event'])
  onInput(event) {
    event.preventDefault();
    event.stopPropagation();

    this.inputSubject.next(event);
  }

  ngOnInit(): void {
    const byNotNullEvents = it => it !== null;
    const andEmit = event => this.debouncedInput.emit(event);

    this.subscription = this.inputSubject
      .pipe(
        filter(byNotNullEvents),
        debounceTime(this.delay)
      )
      .subscribe(andEmit);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
