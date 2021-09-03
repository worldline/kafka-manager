import { Injectable } from '@angular/core'
import { Language } from '@app/models/language.model'
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
/**
* Service managing language
*/
export class LanguageService {
           
    private languages = [
        new Language("en", "assets/img/en.png"),
        new Language("fr", "assets/img/fr.png")
    ];

    constructor(private translate: TranslateService) {}

    /**
    * Returns the list of available languages
    *
    * @return a list corresponding to the available languages
    */
    list(): Language[] {
        return this.languages;
    }

    /**
    * returns the current language
    *
    * @return the current language
    */
    current(): Language {
        const language = this.languages.find(l => l.name == localStorage.getItem("lang"))
        return (language) ? language : this.languages[0]
    }

    /**
    * Save the language
    */
    store(language: Language) {
        this.translate.use(language.name);
        localStorage.setItem("lang", language.name)
    }

}
