import {Directive, ElementRef, Inject, Input, OnInit, PLATFORM_ID} from "@angular/core";
import {isPlatformBrowser} from "@angular/common";

@Directive({
  selector: '[autofocus]'
})
export class AutofocusDirective implements OnInit {
  shouldFocus: boolean;

  @Input() set autofocus(condition: boolean | string) {
    this.shouldFocus = condition !== false && condition !== 'false';
  }

  constructor(private element: ElementRef, @Inject(PLATFORM_ID) private platformId) {
  }

  ngOnInit(): void {
    if (this.shouldFocus && isPlatformBrowser(this.platformId)) {
      setTimeout(() => this.element.nativeElement['focus']());
    }
  }
}
