import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {BrokerService} from '@services/broker.service';
import {Broker} from '@models/broker/broker.model';

@Injectable({
    providedIn: 'root'
})
export class BrokerResolver implements Resolve<Broker> {

    constructor(private brokerService: BrokerService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Broker> {
        return this.brokerService.find(route.paramMap.get('clusterId'), route.paramMap.get('brokerId'));
    }
}
