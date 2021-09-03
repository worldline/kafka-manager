import { Component, OnInit } from '@angular/core';
import { TopicService } from '@services/topic.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import {TopicCreation} from '@models/topic/topic-creation.model';

@Component({
    selector: 'app-topic-create',
    templateUrl: './topic-create.component.html'
})
export class TopicCreateComponent implements OnInit {

    topicForm = new FormGroup({
        name: new FormControl('', Validators.required),
        nbPartitions: new FormControl('', Validators.min(1)),
        replicationFactor: new FormControl('', Validators.min(0)),
        retentionTime: new FormControl(''),
        retentionBytes: new FormControl(''),
    });

    constructor(private route: ActivatedRoute,
                private topicService: TopicService,
                private router: Router,
                private toastr: ToastrService,
                private translate: TranslateService) {}

    ngOnInit(): void {
    }

    onSubmit() {
        this.topicForm.disable();
        // Create topic
        const topicCreation = new TopicCreation(
            this.topicForm.controls.name.value,
            this.topicForm.controls.nbPartitions.value,
            this.topicForm.controls.replicationFactor.value,
            this.topicForm.controls.retentionTime.value,
            this.topicForm.controls.retentionBytes.value,
        );

        // Http call
        const topicName = topicCreation.name;
        const clusterId = this.route.snapshot.data.cluster.id;
        this.topicService.create(clusterId, topicCreation).subscribe(
        () => {
            this.toastr.success(this.translate.instant("topics.create.messages.success.text", {topicName: topicName}), this.translate.instant("topics.create.messages.success.title"));
            this.router.navigateByUrl(`/clusters/${clusterId}/topics/${topicName}`);
        }, () => {
            this.toastr.error(this.translate.instant("topics.create.messages.error.text"), this.translate.instant("topics.create.messages.error.title"));
            this.topicForm.enable();
        }
        );

    }

}
