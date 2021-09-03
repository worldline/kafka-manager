import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {

    @Output() toggleMenuSidebar: EventEmitter<any> = new EventEmitter<any>();

    public searchForm: FormGroup;
    currentLang: string;

    constructor(private translate: TranslateService) {}

    ngOnInit() {
        this.currentLang = this.translate.currentLang || this.translate.getDefaultLang();
        this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
            this.currentLang = event.lang;
        });
        this.searchForm = new FormGroup({
            search: new FormControl(null)
        });
    }

}
