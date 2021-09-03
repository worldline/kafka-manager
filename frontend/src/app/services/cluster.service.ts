import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { BehaviorSubject, Observable, Subject } from 'rxjs'
import { Cluster } from '@models/cluster/cluster.model';
import { Page } from '@models/page.model';
import { ClusterUpdate } from '@models/cluster/cluster-update.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
/**
* Service managing subscription call
*/
export class ClusterService {

    constructor(private httpClient: HttpClient,
                private toastr: ToastrService,
                private translate: TranslateService) {}

    /**
    * Call list
    *
    * @return http call observable
    */
    list(): Observable<Page<Cluster>> {
        return this.httpClient.get<Page<Cluster>>(`/api/clusters/`);
    }

    /**
    * Call create
    *
    * @return http call observable
    */
    create(cluster: ClusterUpdate): Observable<Cluster> {
        return this.httpClient.post<Cluster>(`/api/clusters/`, cluster)
    }

    /**
    * Call find
    *
    * @return http call observable
    */
    find(id: string): Observable<Cluster> {
        return this.httpClient.get<Cluster>(`/api/clusters/${id}`)
    }

    /**
    * Call delete
    *
    * @return http call observable
    */
    delete(id: string): Observable<Cluster> {
        return this.httpClient.delete<Cluster>(`/api/clusters/${id}`)
    }

    /**
    * Call update
    *
    * @return http call observable
    */
    update(clusterId: string, clusterUpdate: ClusterUpdate): Observable<Cluster> {
        return this.httpClient.patch<Cluster>(`/api/clusters/` + clusterId, clusterUpdate)
    }

    //////////////////////////////////////////
    // Selected Cluster management
    //////////////////////////////////////////

    // Observable Cluster
    private selectedClusterSource = new BehaviorSubject<Cluster>(null);

    // Observable Clusters
    private clustersSource = new BehaviorSubject<Cluster[]>(null);

    // Observable string streams
    selectedCluster$ = this.selectedClusterSource.asObservable();

    // Observable string streams
    Clusters$ = this.clustersSource.asObservable();

    saveSelectedCluster(cluster: Cluster) {
        this.selectedClusterSource.next(cluster);
    }

    refreshClusters() {
        this.list().subscribe( data => {
            this.clustersSource.next(data.content);
        }, () => {
            this.toastr.error(this.translate.instant('clusters.messages.error-refresh-list.text'), this.translate.instant('clusters.messages.error-refresh-list.title'));
        });
    }

}
