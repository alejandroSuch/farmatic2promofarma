import {
  Directive,
  ElementRef,
  EventEmitter,
  HostListener,
  Inject,
  Input,
  OnDestroy,
  OnInit,
  Output,
  PLATFORM_ID
} from "@angular/core";

import {BehaviorSubject, Subject, Subscription} from 'rxjs';
import {debounceTime, filter} from 'rxjs/operators';


@Directive({
  selector: '[debouncedInput]'
})
export class InputDebounceDirective implements OnInit, OnDestroy {
  @Input()
  delay: number = 300;

  @Output()
  debouncedInput: EventEmitter<Event> = new EventEmitter<Event>();

  subscription: Subscription;
  inputSubject: Subject<Event> = new BehaviorSubject<Event>(null);

  constructor(private element: ElementRef, @Inject(PLATFORM_ID) private platformId) {
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
