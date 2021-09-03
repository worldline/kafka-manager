import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConsumerGroupService } from '@services/consumer-group.service';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';
import { Pageable } from "@models/pageable.model";

@Component({
    selector: 'app-consumer-groups',
    templateUrl: './consumer-groups.component.html'
})
export class ConsumerGroupsComponent implements OnInit {

    consumerGroups: ConsumerGroup[];
    pageable: Pageable = new Pageable();

    constructor(private route: ActivatedRoute,
                private router: Router,
                private consumerGroupService: ConsumerGroupService) {
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            if (this.route.snapshot.queryParamMap != null) {
                this.pageable.currentPage = this.route.snapshot.queryParamMap['params'].page ? this.route.snapshot.queryParamMap['params'].page : 1;
            }
            const clusterId = params.get('clusterId');
            this.consumerGroupService.list(clusterId, this.pageable).subscribe(consumerGroupPage =>
            {
                this.consumerGroups = consumerGroupPage.content
                this.pageable = Pageable.readPage(consumerGroupPage);
            });
        });
    }

}
