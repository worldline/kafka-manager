import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {ConsumerRecord} from '@models/consumer/consumer-record.model';

@Injectable({
    providedIn: 'root'
})
export class ConsumerService {

    constructor(private httpClient: HttpClient) {
    }

    readMessages(clusterId: string, commit: boolean, numberOfMessages: number, groupId: string, topicName: string, fromBeginning: boolean = true): Observable<ConsumerRecord[]> {

        const httpParams = new HttpParams()
            .append('commit', String(commit))
            .append('consumerGroupId', groupId)
            .append('fromBeginning', String(fromBeginning))
            .append('numberOfMessages', numberOfMessages.toString());

        return this.httpClient.get<ConsumerRecord[]>(`/api/clusters/${clusterId}/consumers/${topicName}`, {params: httpParams})
            .pipe(map((records: ConsumerRecord[]) => {
                records.forEach(record => {
                    try {
                        record.value = JSON.parse(record.value);
                    } catch (e) {
                    }
                    return record;
                });
                return records;
            }));
    }

}
