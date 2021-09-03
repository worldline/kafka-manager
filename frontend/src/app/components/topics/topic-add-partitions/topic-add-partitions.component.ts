import { Component, OnInit } from '@angular/core';
import { TopicService } from '@services/topic.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Topic } from '@models/topic/topic.model';
import { TopicAddPartitions } from '@models/topic/topic-add-partitions.model';

@Component({
    selector: 'topic-add-partitions',
    templateUrl: './topic-add-partitions.component.html'
})
export class TopicAddPartitionsComponent implements OnInit {

    topic: Topic;

    partitionForm = new FormGroup({});

    constructor(private route: ActivatedRoute,
                private topicService: TopicService,
                private router: Router,
                private toastr: ToastrService,
                private translate: TranslateService,
                private formBuilder: FormBuilder) {}

    ngOnInit(): void {
        this.topic = this.route.snapshot.data.topic;
        this.partitionForm.addControl('topicName', new FormControl(this.topic.name, Validators.required));
        this.topic.brokers.forEach(broker => {
            this.partitionForm.addControl(broker.id, this.formBuilder.control(true));
        });
        this.partitionForm.addControl('nbPartitions', new FormControl(this.topic.nbPartitions, Validators.min(0)));
    }

    onSubmit() {
        this.partitionForm.disable();
        // Get assignments
        const assignment: number[] = []
        this.topic.brokers.forEach(broker => {
            if (this.partitionForm.controls[broker.id].value) {
                assignment.push(parseInt(broker.id));
            }
        });
        const assignments: number[][] = [];
        for (let i = this.topic.nbPartitions; i < this.partitionForm.controls.nbPartitions.value; i++) {
            assignments.push(assignment);
        }

        // Create add partitions request
        const topicAddPartitions = new TopicAddPartitions(
            this.partitionForm.controls.nbPartitions.value,
            assignments
        );

        // Http call
        const topicName = this.topic.name;
        const clusterId = this.route.snapshot.data.cluster.id;
        this.topicService.addPartitions(clusterId, topicName, topicAddPartitions).subscribe(
        () => {
            this.toastr.success(this.translate.instant("topics.addPartitions.messages.success.text", {topicName: topicName}), this.translate.instant("topics.addPartitions.messages.success.title"));
            this.router.navigateByUrl(`/clusters/${clusterId}/topics/${topicName}`);
        }, () => {
            this.toastr.error(this.translate.instant("topics.addPartitions.messages.error.text"), this.translate.instant("topics.addPartitions.messages.error.title"));
            this.partitionForm.enable();
        }
        );
    }

}
