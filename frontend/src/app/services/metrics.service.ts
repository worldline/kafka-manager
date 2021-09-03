import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { Page } from '@models/page.model';
import { Metric } from '@models/metrics/metric.model';
import { TopicMetrics } from '@models/topic/topic-metrics.model';

@Injectable({
    providedIn: 'root'
})
/**
 * Service Metrics
 */
export class MetricsService {

    constructor(private httpClient: HttpClient) {
    }

    /**
     * Call list
     *
     * @return http call observable
     */
    list(clusterId: string, topicName: string, startDate: string, endDate: string): Observable<Page<Metric>> {
        return this.httpClient.get<Page<Metric>>(`/api/clusters/${clusterId}/metrics?topicNames=${topicName}&startDate=${startDate}&endDate=${endDate}&sort=date,asc&size=100`);
    }

    listLastMinutes(clusterId: string, lastMinutes: number, topicName?: string): Observable<Page<Metric>> {
        let params = `lastMinutes=${lastMinutes}&sort=date,asc`;
        if (topicName) {
            params += `&topicNames=${topicName}&size=${lastMinutes}`;
        } else {
            params += '&size=2000'
        }
        return this.httpClient.get<Page<Metric>>(`/api/clusters/${clusterId}/metrics?${params}`);
    }

    topicMetric(clusterId: string, topicName: string): Observable<TopicMetrics> {
        return this.httpClient.get<TopicMetrics>(`/api/clusters/${clusterId}/metrics/topics/${topicName}`);
    }

}
