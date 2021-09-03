import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { GlobalSettings } from '@models/global-settings/global-settings.model';

@Injectable({
    providedIn: 'root'
})
/**
 * Service global settings
 */
export class GlobalSettingsService extends GlobalSettings {

    constructor(private httpClient: HttpClient) {
        super();
    }

    init(): Promise<GlobalSettings> {
        return this.get().toPromise().then((settings: GlobalSettings) => {
            Object.assign(this, settings)
            return settings
        })
    }

    get(): Observable<GlobalSettings> {
        return this.httpClient.get<GlobalSettings>(`/api/global-settings`);
    }

}
