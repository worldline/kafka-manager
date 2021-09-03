import { Component, OnInit, ViewChild, HostListener, ElementRef, Renderer2 } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Language } from '@app/models/language.model';
import { LanguageService } from '@app/services/language.service';

@Component({
    selector: 'app-language-dropdown-menu',
    templateUrl: './language-dropdown-menu.component.html',
    styleUrls: ['./language-dropdown-menu.component.scss'],
})
export class LanguageDropdownMenuComponent implements OnInit {

    @ViewChild('dropdownMenu', { static: false }) dropdownMenu;

    selectedLanguage: Language;
    languages: Language[] = [];

    @HostListener('document:click', ['$event'])
    clickout(event) {
        if (!this.elementRef.nativeElement.contains(event.target)) {
            this.hideDropdownMenu();
        }
    }

    constructor(
        private elementRef: ElementRef,
        private renderer: Renderer2,
        public languageService : LanguageService,
        private router: Router
    ) {
        this.router.routeReuseStrategy.shouldReuseRoute = () => {
            return false;
        }

        this.router.events.subscribe((evt) => {
            if (evt instanceof NavigationEnd) {
                this.router.navigated = false;
            }
        });

        this.languages = this.languageService.list();
    }

    ngOnInit() {
        this.selectedLanguage = this.languageService.current();
    }

    onSelect(language: Language): void {
        this.selectedLanguage = language;
        this.languageService.store(language);
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

}
