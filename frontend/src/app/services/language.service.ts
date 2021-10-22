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
        let language : Language;

        // Use language store in session storage
        if (localStorage.getItem("lang") !== null) {
            language = this.languages.find(l => l.name == localStorage.getItem("lang"))
        } 
        
        // Use default language
        if (!language) {
            language = this.languages.find(l => l.name == this.translate.defaultLang)
        }

        // Use first available language
        if (!language) {
            language = this.languages[0]
        }

        // Apply Language
        this.store(language)
        
        return language
    }

    /**
    * Save the language
    */
    store(language: Language) {
        this.translate.use(language.name);
        localStorage.setItem("lang", language.name)
    }

}
