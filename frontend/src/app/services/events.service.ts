import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { Page } from '@models/page.model';
import { Event } from '@models/events/event.model';

@Injectable({
    providedIn: 'root'
})
/**
 * Service Events
 */
export class EventsService {

    constructor(private httpClient: HttpClient) {
    }

    /**
     * Call list
     *
     * @return http call observable
     */
    list(startDate: string, endDate: string): Observable<Page<Event>> {
        return this.httpClient.get<Page<Event>>(`/api/events?startDate=${startDate}&endDate=${endDate}&sort=date,asc&size=100`);
    }

    listLastMinutes(clusterId: string, lastMinutes: number): Observable<Page<Event>> {
        return this.httpClient.get<Page<Event>>(`/api/events?clusterId=${clusterId}&lastMinutes=${lastMinutes}&sort=date,asc&size=200`);
    }

}
