import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';
import { Topic } from '@models/topic/topic.model';
import { ConsumerGroupOffsetPartitionReset } from '@models/consumer-group-offset/consumer-group-offset-partition-reset.model';
import { ConsumerGroupOffsetReset } from '@models/consumer-group-offset/consumer-group-offset-reset.model';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TopicService } from '@services/topic.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ConsumerGroupOffsetService } from '@services/consumer-group-offset.service';

@Component({
    selector: 'app-consumer-group-reset',
    templateUrl: './consumer-group-reset.component.html'
})
export class ConsumerGroupResetComponent implements OnInit {

    clusterId: string;
    consumerGroup: ConsumerGroup;
    topics: Topic[];
    selectedTopic: Topic;
    form: FormGroup;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private topicService: TopicService,
                private consumerGroupOffsetService: ConsumerGroupOffsetService,
                private toastr: ToastrService,
                private translate: TranslateService,) {}

    ngOnInit(): void {
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.consumerGroup = this.route.snapshot.data.consumerGroup;

        // Get topic list
        const topics = [];
        this.consumerGroup.members.filter(m => m.topicPartitions).forEach(m => {
            m.topicPartitions.forEach(tp => {
                topics.push(tp.name);
            });
        });
        this.topicService.list(this.clusterId).subscribe(topicPage => {
            this.topics = topicPage.content.filter(t => topics.includes(t.name));
        });
    }

    selectTopic(topicName: string) {
        this.selectedTopic = null;
        this.topicService.find(this.clusterId, topicName).subscribe(topic => {
            // Create form
            this.form = new FormGroup({});
            topic.partitions.forEach(partition => {
                this.form.addControl(`partition-${partition.partition}`, new FormControl('', Validators.min(0)));
            });
            this.selectedTopic = topic;
        });
    }

    onSubmit() {
        this.form.disable();

        // Create request
        const offsets = [];
        this.selectedTopic.partitions.forEach(partition => {
            const offset = this.form.controls[`partition-${partition.partition}`].value;
            if (offset !== '' && offset !== null) {
                offsets.push(new ConsumerGroupOffsetPartitionReset(this.selectedTopic.name, partition.partition, offset));
            }
        });

        // Check entries
        if (offsets.length < 1) {
            this.toastr.error(this.translate.instant("consumerGroups.reset.messages.unmodify.text"), this.translate.instant("consumerGroups.reset.messages.unmodify.title"));
            this.form.enable();
            return ;
        }

        const request = new ConsumerGroupOffsetReset(offsets);

        // Call http
        this.consumerGroupOffsetService.reset(this.clusterId, this.consumerGroup.groupId, request).subscribe(
        () => {
            this.toastr.success(this.translate.instant("consumerGroups.reset.messages.success.text", {groupId: this.consumerGroup.groupId}), this.translate.instant("consumerGroups.reset.messages.success.title"));
            this.router.navigateByUrl(`/clusters/${this.clusterId}/consumer-groups/${this.consumerGroup.groupId}`);
        }, () => {
            this.toastr.error(this.translate.instant("consumerGroups.reset.messages.error.text"), this.translate.instant("consumerGroups.reset.messages.error.title"));
            this.form.enable();
        });
    }

}
