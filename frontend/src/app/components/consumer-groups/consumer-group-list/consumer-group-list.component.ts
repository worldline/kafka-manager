import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConsumerGroupInfo} from '@models/consumer-group/consumer-group-info.model';
import {ConsumerGroupOverview} from "@models/consumer-group/consumer-group-overview.model";
import {Pageable} from "@models/pageable.model";

@Component({
    selector: 'app-consumer-group-list',
    templateUrl: './consumer-group-list.component.html'
})
export class ConsumerGroupListComponent implements OnInit {

    @Input() consumerGroups: ConsumerGroupOverview[];
    routeToConsumer: string;
    routeToTopic: string;
    clusterId: string;
    @Input() pageable: Pageable;

    constructor(private route: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.routeToConsumer = `/clusters/${this.clusterId}/consumer-groups`;
        this.routeToTopic = `/clusters/${this.clusterId}/topics`;
    }

    getStateClass(consumerGroup: ConsumerGroupOverview): string{
        return ConsumerGroupInfo.getStatusTextClass(consumerGroup.state)
    }

    getStateIcon(consumerGroup: ConsumerGroupOverview): string{
        return ConsumerGroupInfo.getStatusIcon(consumerGroup.state)
    }

    pageChange(newPage: number) {
        this.pageable.currentPage = newPage;
        this.router.navigate([`/clusters/${this.clusterId}/consumer-groups`], { queryParams: { page: newPage } });
    }
}
