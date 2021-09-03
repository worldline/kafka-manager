import {HttpClient} from '@angular/common/http'
import {TranslateLoader} from '@ngx-translate/core'
import {forkJoin, Observable} from 'rxjs'
import {map} from 'rxjs/operators'

/**
 * Internal impl for files translation loader (manage multiple json file)
 */
export class MultiTranslateHttpLoader implements TranslateLoader {
    // eslint-disable-next-line require-jsdoc
    constructor(
        private http: HttpClient,
        public resources: {
            prefix: string
            suffix: string
        }[] = [
            {prefix: './assets/i18n/', suffix: '.json'},
            {prefix: './assets/i18n/breadcrumb/', suffix: '.json'},
            {prefix: './assets/i18n/brokers/', suffix: '.json'},
            {prefix: './assets/i18n/clusters/', suffix: '.json'},
            {prefix: './assets/i18n/dashboard/', suffix: '.json'},
            {prefix: './assets/i18n/side-menu/', suffix: '.json'},
            {prefix: './assets/i18n/topic-detail/', suffix: '.json'},
            {prefix: './assets/i18n/settings/', suffix: '.json'},
            {prefix: './assets/i18n/topics/', suffix: '.json'},
            {prefix: './assets/i18n/consumer-groups/', suffix: '.json'},
            {prefix: './assets/i18n/messages/', suffix: '.json'},
            {prefix: './assets/i18n/contacts/', suffix: '.json'},
            {prefix: './assets/i18n/metrics/', suffix: '.json'},
            {prefix: './assets/i18n/connect/', suffix: '.json'},
            {prefix: './assets/i18n/login/', suffix: '.json'}
        ]) {
    }

    /**
     * Get translation in files, make a join on all files (key must not be duplicate in different file)
     *
     * @param lang current language
     *
     * @return found translation
     */
    getTranslation(lang: string): Observable<any> { // eslint-disable-line @typescript-eslint/no-explicit-any
        return forkJoin(this.resources.map(config => {
            return this.http.get(`${config.prefix}${lang}${config.suffix}`)
        })).pipe(map(response => {
            return response.reduce((a, b) => {
                return Object.assign(a, b)
            })
        }))
    }
}
