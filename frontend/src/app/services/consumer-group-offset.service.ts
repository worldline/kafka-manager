import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { ConsumerGroupOffsetReset } from '@models/consumer-group-offset/consumer-group-offset-reset.model';

@Injectable({
    providedIn: 'root'
})
/**
 * Service consumer groups offset
 */
export class ConsumerGroupOffsetService {

    constructor(private httpClient: HttpClient) {
    }

    reset(clusterId: string, groupId: string, request: ConsumerGroupOffsetReset): Observable<void> {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/consumer-groups-offset/${groupId}/reset`, request);
    }

    copyOffset(clusterId: string, sourceGroupId: string, targetGroupId: string, offset: number): Observable<void> {
        const body = new HttpParams().set('offset', offset.toString());
        return this.httpClient.post<void>(`/api/clusters/${clusterId}/consumer-groups-offset/${sourceGroupId}/from/${targetGroupId}`, body);
    }

}
