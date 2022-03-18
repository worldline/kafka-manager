import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { Page } from '@models/page.model';
import { Plugin } from '@models/connect/plugin.model';
import { Connector } from '@models/connect/connector.model';
import { CreateConnector } from '@models/connect/create-connector.model';
import { UpdateConnector } from '@models/connect/update-connector.model';
import { ValidatePluginRequest } from '@models/connect/validate-plugin-request.model';
import { ValidatePluginResponse } from '@models/connect/validate-plugin-response.model';
import { Pageable } from "@models/pageable.model";

@Injectable({
    providedIn: 'root'
})
/**
* Service managing kafka connect call
*/
export class ConnectService {

    constructor(private httpClient: HttpClient) { }

    /**
     * Call list
     *
     * @return http call observable
     */
    connectors(clusterId: string, pageable: Pageable = null): Observable<Page<Connector>> {
        const httpParams = new HttpParams()
            .append('size', pageable != null ? String(pageable.itemsPerPage) : String(''))
            .append('page', pageable != null ? String(pageable.currentPage - 1) : String(''));
        return this.httpClient.get<Page<Connector>>(`/api/clusters/${clusterId}/kafka-connect/connectors`, {params: httpParams});
    }

    connector(clusterId: string, connectorId: string): Observable<Connector> {
        return this.httpClient.get<Connector>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}`);
    }

    createConnector(clusterId: String, createConnector: CreateConnector): Observable<Connector> {
        return this.httpClient.post<Connector>(`/api/clusters/${clusterId}/kafka-connect/connectors`, createConnector);
    }

    updateConnector(clusterId: String, connectorId: string, updateConnector: UpdateConnector): Observable<void> {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}/config`, updateConnector);
    }

    deleteConnector(clusterId: String, connectorId: string): Observable<void> {
        return this.httpClient.delete<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}`);
    }

    plugins(clusterId: string): Observable<Page<Plugin>> {
        return this.httpClient.get<Page<Plugin>>(`/api/clusters/${clusterId}/kafka-connect/connector-plugins`);
    }

    validatePlugin(clusterId: string, request: ValidatePluginRequest): Observable<ValidatePluginResponse> {
        let pluginName = request.config['connector.class'];
        pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1);
        return this.httpClient.put<ValidatePluginResponse>(`/api/clusters/${clusterId}/kafka-connect/connector-plugins/${pluginName}/config/validate`, request);
    }

    restartConnector(clusterId: string, connectorId: string): Observable<void> {
        return this.httpClient.post<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}/restart`, null);
    }

    pauseConnector(clusterId: string, connectorId: string): Observable<void> {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}/pause`, null);
    }

    resumeConnector(clusterId: string, connectorId: string): Observable<void> {
        return this.httpClient.put<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}/resume`, null);
    }

    restartTask(clusterId: string, connectorId: string, taskId: string): Observable<void> {
        return this.httpClient.post<void>(`/api/clusters/${clusterId}/kafka-connect/connectors/${connectorId}/tasks/${taskId}/restart`, null);
    }
    

}
