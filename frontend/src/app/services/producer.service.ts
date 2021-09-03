import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {ProducerRecord} from '@models/producer/producer-record.model';
import {ProducerMessages} from '@models/producer/producer-messages.model';
import {ProducerMessage} from '@models/producer/producer-message.model';

@Injectable({
    providedIn: 'root'
})
export class ProducerService {

    constructor(private httpClient: HttpClient) {
    }

    sendMessages(clusterId: String, topicName: string, messages: ProducerMessage[]): Observable<ProducerRecord> {
        const body: ProducerMessages = new ProducerMessages(messages);
        return this.httpClient.post<ProducerRecord>(`/api/clusters/${clusterId}/producers/${topicName}`, body);
    }

}
