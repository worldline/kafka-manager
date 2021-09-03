import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { Md5 } from 'ts-md5';

@Directive({
    selector: '[appGravatar]'
})
export class GravatarDirective implements OnInit {

    @Input() email: string;

    @Input() size: number;

    fallback = 'identicon'; // https://en.gravatar.com/site/implement/images/

    constructor(private el: ElementRef) {
    }

    ngOnInit(): void {
        this.update();
    }

    update(): void {
        if (!this.el.nativeElement) {
            return
        }

        const emailHash = (!this.email) ? "empty" : Md5.hashStr(this.email.trim().toLowerCase()).toString();
        const size = (!this.size) ? 80 : this.size;

        this.el.nativeElement.src = `http://www.gravatar.com/avatar/${emailHash}?d=${this.fallback}&s=${size}`;
    }

}
