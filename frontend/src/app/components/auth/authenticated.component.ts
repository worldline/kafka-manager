import { Component, Renderer2, ViewChild } from '@angular/core';

@Component({
    selector: 'app-authenticated',
    templateUrl: './authenticated.component.html'
})
export class AuthenticatedComponent {

    public sidebarMenuOpened = true;
    @ViewChild('contentWrapper', { static: false }) contentWrapper;

    constructor(private renderer: Renderer2) { }

    mainSidebarHeight(height) {
        // this.renderer.setStyle(
        //   this.contentWrapper.nativeElement,
        //   'min-height',
        //   height - 114 + 'px'
        // );
    }

    toggleMenuSidebar() {
        if (this.sidebarMenuOpened) {
            this.renderer.removeClass(document.querySelector('app-root'),'sidebar-open');
            this.renderer.addClass(document.querySelector('app-root'),'sidebar-collapse');
            this.sidebarMenuOpened = false;
        } else {
            this.renderer.removeClass(document.querySelector('app-root'),'sidebar-collapse');
            this.renderer.addClass(document.querySelector('app-root'),'sidebar-open');
            this.sidebarMenuOpened = true;
        }
    }
}
