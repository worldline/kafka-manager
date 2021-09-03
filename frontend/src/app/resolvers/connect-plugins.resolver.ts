import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {ConnectService} from '@services/connect.service';
import {Plugin} from '@models/connect/plugin.model';
import {map} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ConnectPluginsResolver implements Resolve<Plugin[]> {

    constructor(private connectService: ConnectService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Plugin[]> {
        return this.connectService.plugins(route.paramMap.get('clusterId'))
            .pipe(map(data => data.content));
    }
}
