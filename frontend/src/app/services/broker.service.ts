import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { Page } from '@models/page.model';
import { Broker } from '@models/broker/broker.model';
import { BrokerSearch } from '@models/broker/broker-search.model';
import { Setting } from '@models/setting/setting.model';
import { Pageable } from "@models/pageable.model";

@Injectable({
    providedIn: 'root'
})
/**
* Service Broker
*/
export class BrokerService {

    constructor(private httpClient: HttpClient) {}

    /**
    * Call list
    *
    * @return http call observable
    */
    list(clusterId: string, brokerSearch: BrokerSearch, pageable: Pageable = null): Observable<Page<Broker>> {
        let params = new HttpParams()
            .set('full', brokerSearch.full.toString())
            .set('size', pageable != null ? String(pageable.itemsPerPage) : '')
            .set('page', pageable != null ? String(pageable.currentPage - 1) : '');
        return this.httpClient.get<Page<Broker>>("/api/clusters/" + clusterId + "/brokers", { params: params });
    }

    find(clusterId: string, brokerId: string): Observable<Broker> {
        return this.httpClient.get<Broker>(`/api/clusters/${clusterId}/brokers/${brokerId}`);
    }

    updateSettings(clusterId: string, brokerId: string, data: Setting[]) {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/brokers/${brokerId}/settings`, {configuration: data});
    }

}
