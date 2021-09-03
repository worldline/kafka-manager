import { Directive, Input, OnChanges, ElementRef, HostListener, SimpleChanges } from '@angular/core';
import * as $ from 'jquery';

@Directive({
    selector: '[data-card-widget]'
})
export class CardActionsDirective implements OnChanges {
  @Input('data-card-widget') type: string;
  @Input() isOpen: boolean = false;
  private isTrigger: boolean = false;
  private animationSpeed: string = 'normal';
  private collapseIcon: string = 'fa-minus';
  private expandIcon: string = 'fa-plus';
  private maximizeIcon: string = 'fa-expand';
  private minimizeIcon: string = 'fa-compress';

  constructor(private el: ElementRef) {}

    ngOnChanges(changes: SimpleChanges): void {
        if(changes.isOpen && changes.isOpen.previousValue !== undefined){
            this.manageEvent(!this.isOpen);

            // Update status
            this.isTrigger = !this.isOpen;
        }
    }

  @HostListener('click')
  onClick() {
    this.manageEvent(!this.isTrigger);

    // Update status
    this.isTrigger = !this.isTrigger;
  }

  manageEvent(isOpen: boolean) {
    const el = $(this.el.nativeElement);
    const type = el.attr('data-card-widget');

      // Get card
      const card = el.closest('.card');
      if (!card) {
        return ;
      }

      if (type === 'collapse') {
        this.collapse(el, card, isOpen);
      } else if (type === 'maximize') {
        this.maximize(el, card, isOpen);
      }
  }

  collapse(el, card, isOpen: boolean) {
    const icon = el.find('i');
    const elements = card.find('.card-body, .card-footer');
    if (isOpen) {
      elements.slideUp(this.animationSpeed);
      icon.addClass(this.expandIcon).removeClass(this.collapseIcon);
    } else {
      elements.slideDown(this.animationSpeed);
      icon.addClass(this.collapseIcon).removeClass(this.expandIcon);
    }
  }

  maximize(el, card, isOpen: boolean) {
    // Update css class
    const icon = el.find('i');
    if (isOpen) {
      icon.addClass(this.minimizeIcon).removeClass(this.maximizeIcon);
      card.addClass('maximized-card');
      card.css({
        height: card.height(),
        width: card.width(),
        transition: 'all .15s'
      });
    } else {
      icon.addClass(this.maximizeIcon).removeClass(this.minimizeIcon);
      card.removeClass('maximized-card');
      card.css({
        height: 'inherit',
        width: 'inherit'
      });
    }
  }
}
