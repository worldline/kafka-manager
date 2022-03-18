import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { TopicCreation } from '@models/topic/topic-creation.model';
import { Page } from '@models/page.model';
import { Setting } from '@models/setting/setting.model';
import { TopicReassign } from '@models/topic/topic-reassign.model';
import { TopicAddPartitions } from '@models/topic/topic-add-partitions.model';
import { Topic } from '@models/topic/topic.model';
import { Pageable } from "@models/pageable.model";

@Injectable({
    providedIn: 'root'
})
/**
 * Service Topic
 */
export class TopicService {

    constructor(private httpClient: HttpClient) {
    }

    /**
     * Call list
     *
     * @return http call observable
     */
    list(clusterId: string, full: boolean = false, text: string = null, pageable: Pageable = null): Observable<Page<Topic>> {
        let httpParams = new HttpParams()
            .append('sort', String('name'))
            .append('full', String(full))
            .append('size', pageable != null ? String(pageable.itemsPerPage) : String(''))
            .append('page', pageable != null ? String(pageable.currentPage - 1) : String(''));
        if (text) {
            httpParams = httpParams.append('topicName', String(text));
        }

        return this.httpClient.get<Page<Topic>>(`/api/clusters/${clusterId}/topics`, {params: httpParams});
    }

    find(clusterId: string, topicName: string): Observable<Topic> {
        return this.httpClient.get<Topic>(`/api/clusters/${clusterId}/topics/${topicName}`);
    }

    create(clusterId: String, topicCreation: TopicCreation) {
        return this.httpClient.post<Topic>(`/api/clusters/${clusterId}/topics/`, topicCreation);
    }

    delete(clusterId: string, topicName: string) {
        return this.httpClient.delete<void>(`/api/clusters/${clusterId}/topics/${topicName}`);
    }

    reassign(clusterId: string, topicName: string, topicReassign: TopicReassign) {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/topics/${topicName}/partitions/reassign`, topicReassign);
    }

    updateSettings(clusterId: string, topicName: string, data: Setting[]) {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/topics/${topicName}/settings`, {configuration: data});
    }

    addPartitions(clusterId: string, topicName: string, data: TopicAddPartitions) {
        return this.httpClient.post<void>(`/api/clusters/${clusterId}/topics/${topicName}/partitions`, data);
    }
}
