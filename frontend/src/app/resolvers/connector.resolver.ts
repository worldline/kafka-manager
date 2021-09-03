import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {ConnectService} from '@services/connect.service';
import {Connector} from '@models/connect/connector.model';

@Injectable({
    providedIn: 'root'
})
export class ConnectorResolver implements Resolve<Connector> {

    constructor(private connectService: ConnectService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Connector> {
        return this.connectService.connector(route.paramMap.get('clusterId'), route.paramMap.get('connectorName'));
    }
}
