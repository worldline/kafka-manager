import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {Cluster} from '@models/cluster/cluster.model'
import {ClusterService} from '@services/cluster.service'
import {tap} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ClusterResolver implements Resolve<Cluster> {

    constructor(private clusterService: ClusterService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Cluster> {
        return this.clusterService.find(route.paramMap.get('clusterId'))
            .pipe(tap(cluster => this.clusterService.saveSelectedCluster(cluster)));
    }
}
