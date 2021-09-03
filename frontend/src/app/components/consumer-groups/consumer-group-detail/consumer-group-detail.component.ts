import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConsumerGroupService} from '@services/consumer-group.service';
import {ConsumerGroup} from '@models/consumer-group/consumer-group.model';
import {TranslateService} from '@ngx-translate/core';
import {ToastrService} from 'ngx-toastr';
import {CardIndicator} from '@models/card-indicator/card-indicator.model';
import {ConsumerGroupStatusCardIndicator} from '@models/card-indicator/consumer-group/status-card-indicator.model';
import {ConsumerGroupLagCardIndicator} from "@models/card-indicator/consumer-group/lag-card-indicator.model";

@Component({
    selector: 'app-consumer-group-detail',
    templateUrl: './consumer-group-detail.component.html',
    styleUrls: ['./consumer-group-detail.component.css']
})
export class ConsumerGroupDetailComponent implements OnInit {

    consumerGroup: ConsumerGroup;
    deleteModalState: boolean = false;
    cardIndicators : CardIndicator[];
    computedLag:  number = 0;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private consumerGroupService: ConsumerGroupService,
                private toastr: ToastrService,
                private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.consumerGroup = this.route.snapshot.data.consumerGroup;

        this.initCards();
    }

    deleteGroup() {
        const clusterId = this.route.snapshot.data.cluster.id;
        const groupId = this.consumerGroup.groupId;
        this.consumerGroupService.delete(clusterId, groupId).subscribe(
            () => {
                this.toastr.success(this.translate.instant("consumerGroups.detail.delete.messages.success.text", {groupId: groupId}), this.translate.instant("consumerGroups.detail.delete.messages.success.title"));
                this.router.navigateByUrl(`/clusters/${clusterId}/consumer-groups`);
            }, () => {
                this.toastr.error(this.translate.instant("consumerGroups.detail.delete.messages.error.text"), this.translate.instant("consumerGroups.detail.delete.messages.error.title"));
            }
        );
    }

    resetOffset() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/consumer-groups/${this.consumerGroup.groupId}/reset-offsets`);
    }

    initCards() {
        this.cardIndicators = [
            new ConsumerGroupStatusCardIndicator(this.consumerGroup.state),
            new ConsumerGroupLagCardIndicator(this.computedLag.toString())
        ]
    }

    setComputedLag(value: number) {
        this.computedLag  = value;
        this.initCards();
    }

}
