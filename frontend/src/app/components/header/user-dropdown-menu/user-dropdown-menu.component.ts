import { Component, OnInit, ViewChild, HostListener, ElementRef, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '@models/user.model';
import { AuthService } from '@services/auth.service';
import { HelperService } from '@services/helper.service';
import { UserService } from '@services/user.service';

@Component({
    selector: 'app-user-dropdown-menu',
    templateUrl: './user-dropdown-menu.component.html',
    styleUrls: ['./user-dropdown-menu.component.scss'],
})
export class UserDropdownMenuComponent implements OnInit {
    public user: User;

    @ViewChild('dropdownMenu', { static: false }) dropdownMenu;
    @HostListener('document:click', ['$event'])
    clickout(event) {
        if (!this.elementRef.nativeElement.contains(event.target)) {
            this.hideDropdownMenu();
        }
    }

    constructor(
        private elementRef: ElementRef,
        public helperService: HelperService,
        private renderer: Renderer2,
        private router: Router,
        private userService: UserService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.user = this.userService.currentUser();
    }

    toggleDropdownMenu() {
        if (this.dropdownMenu.nativeElement.classList.contains('show')) {
            this.hideDropdownMenu();
        } else {
            this.showDropdownMenu();
        }
    }

    showDropdownMenu() {
        this.renderer.addClass(this.dropdownMenu.nativeElement, 'show');
    }

    hideDropdownMenu() {
        this.renderer.removeClass(this.dropdownMenu.nativeElement, 'show');
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('/login');
    }
}
