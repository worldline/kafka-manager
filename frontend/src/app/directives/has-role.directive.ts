import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2 } from "@angular/core";
import { AuthService } from "@services/auth.service";
import { Subscription } from "rxjs";

@Directive({
    selector: "[hasRole]"
})
export class HasRoleDirective implements OnInit, OnDestroy {

    // the role the user must have
    @Input('hasRole') inputRole: string;

    private subscription: Subscription

    constructor(
        private renderer: Renderer2,
        private elementRef: ElementRef,
        private authService: AuthService
    ) { }

    ngOnInit() {
        const displayValue = this.elementRef.nativeElement.style.display
        this.renderer.setStyle(this.elementRef.nativeElement, 'display', 'none');

        this.subscription = this.authService.roles$.subscribe(roles => {
            if (roles && roles.includes(this.inputRole)) {
                this.renderer.setStyle(this.elementRef.nativeElement, 'display', displayValue);
            }
        });
    }

    // Clear the subscription on destroy
    ngOnDestroy() {
        this.subscription.unsubscribe()
    }
}
