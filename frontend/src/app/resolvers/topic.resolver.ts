import {Injectable} from '@angular/core'
import {ActivatedRouteSnapshot, Resolve} from '@angular/router'
import {Observable} from 'rxjs'
import {TopicService} from '@services/topic.service';
import {Topic} from '@models/topic/topic.model';

@Injectable({
    providedIn: 'root'
})
export class TopicResolver implements Resolve<Topic> {

    constructor(private topicService: TopicService) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<Topic> {
        return this.topicService.find(route.paramMap.get('clusterId'), route.paramMap.get('topicName'));
    }
}
