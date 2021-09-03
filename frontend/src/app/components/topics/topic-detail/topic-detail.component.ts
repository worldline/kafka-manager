import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Topic } from '@models/topic/topic.model';
import { TranslateService } from '@ngx-translate/core';
import { TopicService } from '@services/topic.service';
import { ToastrService } from 'ngx-toastr';
import { ConsumerGroupService } from '@services/consumer-group.service';
import { ConsumerMember } from '@models/consumer-group/consumer-member.model';
import { GlobalSettingsService } from '@services/global-settings.service';
import { RatioCardIndicator } from '@models/card-indicator/ratio-card-indicator.model';
import { SimpleCardIndicator } from '@models/card-indicator/simple-card-indicator.model';

@Component({
    selector: 'app-topic-detail',
    templateUrl: './topic-detail.component.html',
    styleUrls: ['./topic-detail.component.css']
})
export class TopicDetailComponent implements OnInit {

    deleteModalState: boolean = false;
    topic: Topic;
    consumerGroupMembers: ConsumerMember[];
    topicLag: number;
    cardIndicators: RatioCardIndicator[];

    constructor(private route: ActivatedRoute,
                private router: Router,
                private toastr: ToastrService,
                private topicService: TopicService,
                private consumerGroupService: ConsumerGroupService,
                public settings: GlobalSettingsService,
                private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.topic = this.route.snapshot.data.topic;
        const clusterId = this.route.snapshot.data.cluster.id;
        this.consumerGroupService.findByTopic(clusterId, this.topic.name).subscribe(consumerGroups => {
            let result = [];
            consumerGroups.content.forEach(m => {
                result = result.concat(m.members);
            });
            this.consumerGroupMembers = result;

            // Compute topic lag if only one consumer exists
            this.computeTopicLag();
        });

        this.cardIndicators = [
            new RatioCardIndicator(null, "topics.details.label.brokersSpread", this.indiceComputation(this.topic.brokersSpread, 'asc'), true, this.topic.brokersSpread.toString(), this.ratioComputation(this.topic.brokersSpread)),
            new RatioCardIndicator(null, "topics.details.label.brokersSkewed", this.indiceComputation(this.topic.brokersSkewed, 'desc'), true, this.topic.brokersSkewed.toString(), this.ratioComputation(this.topic.brokersSkewed)),
            new RatioCardIndicator(null, "topics.details.label.brokersUnderReplicated", this.indiceComputation(this.topic.nbUnderReplication, 'desc'), true, this.topic.nbUnderReplication.toString(), this.ratioComputation(this.topic.nbUnderReplication))
        ];
    }

    computeTopicLag() {
        if (this.consumerGroupMembers && this.consumerGroupMembers.length === 1) {
            let topicEnd = 0, topicCurrent = 0;
            this.consumerGroupMembers[0].topicPartitions.filter(partition => {
                return partition.offsets && partition.offsets.current >= 0;
            }).forEach(partition => {
                topicEnd += partition.offsets.end;
                topicCurrent += partition.offsets.current;
            });
            if (topicEnd > 0) {
                this.topicLag = topicEnd - topicCurrent;
            }
        }
    }

    deleteTopic() {
        const clusterId = this.route.snapshot.data.cluster.id;
        this.topicService.delete(clusterId, this.topic.name).subscribe(
            () => {
                this.toastr.success(this.translate.instant("topics.details.delete.messages.success.text", {topicName: this.topic.name}), this.translate.instant("topics.details.delete.messages.success.title"));
                this.router.navigateByUrl(`/clusters/${clusterId}/topics`);
            }, () => {
                this.toastr.error(this.translate.instant("topics.details.delete.messages.error.text"), this.translate.instant("topics.details.delete.messages.error.title"));
            }
        );
    }

    goToUpdateSettings() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/topics/${this.topic.name}/settings/edit`);
    }

    goToAddPartition() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/topics/${this.topic.name}/add-partitions`);
    }

    goToReassignPartition() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/topics/${this.topic.name}/reassign-partitions`);
    }

    goToTopics() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/topics`);
    }

    computeGauge(value: number) {
        return value * 1.8;
    }

    ratioComputation(value: number): string {
        return this.computeGauge(value) + "deg";
    }

    indiceComputation(value: number, order: String): number {
        const result = this.computeGauge(value);
        if(result < 33)  return (order === "asc") ? 2 : 0;
        if(result < 66)  return 1;
        return (order === "asc") ? 0 : 2;
    }

}
