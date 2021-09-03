import { Component, OnInit } from '@angular/core';
import { TopicService } from '@services/topic.service';
import { BrokerService } from '@services/broker.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Topic } from '@models/topic/topic.model';
import { TopicReassign } from '@models/topic/topic-reassign.model';
import { TopicReassignPartition } from '@models/topic/topic-reassign-partition.model';
import { BrokerSearch } from '@models/broker/broker-search.model';
import { forkJoin } from 'rxjs';
import * as $ from 'jquery';

@Component({
    selector: 'topic-reassign-partitions',
    templateUrl: './topic-reassign-partitions.component.html'
})
export class TopicReassignPartitionsComponent implements OnInit {

    topic: Topic;
    clusterId: string;

    partitionForm = new FormGroup({});
    brokerIds: number[];
    constructor(private route: ActivatedRoute,
                private topicService: TopicService,
                private brokerService: BrokerService,
                private router: Router,
                private toastr: ToastrService,
                private translate: TranslateService,
                private formBuilder: FormBuilder) {}

    ngOnInit(): void {
        // Init data
        this.topic = this.route.snapshot.data.topic;
        this.clusterId = this.route.snapshot.data.cluster.id;
        const brokers = this.route.snapshot.data.brokers;
        this.brokerIds = brokers.map(broker => broker.id);

        this.topic.partitions.forEach(partition => {
            this.partitionForm.addControl('replicas-' + partition.partition, new FormControl(partition.replicas.map(r => parseInt(r))));
        });
    }

    onSubmit() {
        this.partitionForm.disable();

        // Create request
        const partitions: TopicReassignPartition[] = this.topic.partitions.filter(partition => {
            const newData = this.partitionForm.controls['replicas-' + partition.partition].value || [];
            let same = true;
            newData.forEach(d => {
                if (!partition.replicas.includes(d.toString())) {
                    same = false;
                }
            });
            return !same || newData.length != partition.replicas.length;
        }).map(partition => {
            const newData = this.partitionForm.controls['replicas-' + partition.partition].value || [];
            return new TopicReassignPartition(partition.partition, newData);
        });
        const request = new TopicReassign(partitions);

        // Manage success
        this.topicService.reassign(this.clusterId, this.topic.name, request).subscribe(
        () => {
            this.toastr.success(this.translate.instant("topics.reassignPartitions.messages.success.text", {topicName: this.topic.name}), this.translate.instant("topics.reassignPartitions.messages.success.title"));
            this.router.navigateByUrl(`/clusters/${this.clusterId}/topics/${this.topic.name}`);
        }, () => {
            this.toastr.error(this.translate.instant("topics.reassignPartitions.messages.error.text"), this.translate.instant("topics.reassignPartitions.messages.error.title"));
            this.partitionForm.enable();
        }
        );
    }

}
