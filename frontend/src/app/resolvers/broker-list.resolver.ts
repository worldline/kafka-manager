import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {BrokerService} from '@services/broker.service';
import {Broker} from '@models/broker/broker.model';
import {BrokerSearch} from '@models/broker/broker-search.model';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class BrokerListResolver implements Resolve<Broker[]> {

    constructor(private brokerService: BrokerService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Broker[]> {
        return this.brokerService.list(route.paramMap.get('clusterId'), new BrokerSearch(false))
          .pipe(map(data => data.content));
    }
}
