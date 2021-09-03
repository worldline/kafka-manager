import { Component, OnInit, Renderer2 } from '@angular/core';
import {
    Event,
    NavigationCancel,
    NavigationEnd,
    NavigationError,
    NavigationStart,
    Router
} from '@angular/router';
import { NgxSpinnerService } from "ngx-spinner";
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

    constructor(private renderer: Renderer2,
        private router: Router,
        private spinner: NgxSpinnerService,
        private translate: TranslateService) {
            const langStorage = localStorage.getItem("lang");
            if (langStorage) {
                this.translate.use(langStorage);
            }
            this.router.events.subscribe((event: Event) => {
                switch (true) {
                    case event instanceof NavigationStart: {
                        spinner.show('spinner-global');
                        break;
                    }
                    case event instanceof NavigationEnd:
                    case event instanceof NavigationCancel:
                    case event instanceof NavigationError: {
                        spinner.hide('spinner-global');
                        break;
                    }
                    default: {
                        break;
                    }
                }
            });
    }

    ngOnInit() {
        this.renderer.removeClass(document.querySelector('app-root'), 'login-page');
        this.renderer.removeClass(document.querySelector('app-root'), 'register-page');
    }

}
