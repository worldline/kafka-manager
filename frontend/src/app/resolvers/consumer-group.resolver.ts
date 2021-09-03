import { Injectable } from '@angular/core'
import { ActivatedRouteSnapshot, Resolve } from '@angular/router'
import { Observable } from 'rxjs'
import { ConsumerGroupService } from '@services/consumer-group.service';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';

@Injectable({
    providedIn: 'root'
})
export class ConsumerGroupResolver implements Resolve<ConsumerGroup> {

    constructor(private consumerGroupService: ConsumerGroupService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<ConsumerGroup> {
      return this.consumerGroupService.find(route.paramMap.get('clusterId'), route.paramMap.get('consumerGroupId'));
    }
}
