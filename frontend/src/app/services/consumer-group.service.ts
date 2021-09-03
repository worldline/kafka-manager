import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { Page } from '@models/page.model';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';
import { Pageable } from "@models/pageable.model";

@Injectable({
    providedIn: 'root'
})
/**
 * Service consumer groups
 */
export class ConsumerGroupService {

    constructor(private httpClient: HttpClient) {
    }

    /**
     * Call list
     *
     * @return http call observable
     */
    list(clusterId: string, pageable: Pageable = null): Observable<Page<ConsumerGroup>> {
        let url = `/api/clusters/${clusterId}/consumer-groups?sort=groupId`;
        if (pageable != null) {
           url = url.concat(`&size=${pageable.itemsPerPage}&page=${pageable.currentPage - 1}`);
        }
        return this.httpClient.get<Page<ConsumerGroup>>(url);
    }

    find(clusterId: string, groupId: string): Observable<ConsumerGroup> {
        return this.httpClient.get<ConsumerGroup>(`/api/clusters/${clusterId}/consumer-groups/${groupId}`);
    }

    findByTopic(clusterId: string, topicName: string, getOffsets: boolean = true): Observable<Page<ConsumerGroup>> {
        return this.httpClient.get<Page<ConsumerGroup>>(`/api/clusters/${clusterId}/consumer-groups/describes?topicNames=${topicName}&offsets=${getOffsets}`);
    }

    delete(clusterId: string, groupId: string) {
        return this.httpClient.delete<void>(`/api/clusters/${clusterId}/consumer-groups?groupIds=${groupId}`);
    }

}
