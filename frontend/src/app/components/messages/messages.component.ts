import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {TopicService} from '@services/topic.service';
import {ConsumerGroupService} from '@services/consumer-group.service';
import {ConsumerGroupOffsetService} from '@services/consumer-group-offset.service';
import {ConsumerService} from '@services/consumer.service';
import {ProducerService} from '@services/producer.service';
import {Topic} from '@models/topic/topic.model';
import {ConsumerGroup} from '@models/consumer-group/consumer-group.model';
import {ConsumerRecord} from '@models/consumer/consumer-record.model';
import {JsonViewerComponent} from '@components/global/json-viewer/json-viewer.component';
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';
import * as cloneDeep from 'lodash/cloneDeep';
import {ProducerMessage} from '@models/producer/producer-message.model';
import { NgxSpinnerService } from 'ngx-spinner';
import { delay } from 'rxjs/operators';

@Component({
    selector: 'app-messages',
    templateUrl: './messages.component.html',
    styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {

    static groupId: string;
    form = new FormGroup({
        commit: new FormControl(false),
        numberOfMessages: new FormControl(10, [Validators.required, Validators.min(0)]),
        groupToUse: new FormControl(''),
        topic: new FormControl('', Validators.required),
        groupId: new FormControl('', Validators.required),
        addOffset: new FormControl(0, Validators.required)
    });
    editForm = new FormGroup({
        topic: new FormControl('', Validators.required)
    });
    records: ConsumerRecord[];
    initRecords: ConsumerRecord[];
    topics: Topic[];
    consumerGroups: ConsumerGroup[];
    editedRecord: ConsumerRecord;
    confirmCopyModal: boolean = false;
    private selectedRecords: ConsumerRecord[] = [];
    @ViewChild(JsonViewerComponent) child;
    showMore: boolean = false;
    messagesSpinnerId: string = 'spinner-messages';

    constructor(private route: ActivatedRoute,
                private consumerService: ConsumerService,
                private producerService: ProducerService,
                private toastr: ToastrService,
                private translate: TranslateService,
                private topicService: TopicService,
                private consumerGroupOffsetService: ConsumerGroupOffsetService,
                private consumerGroupService: ConsumerGroupService,
                private spinner: NgxSpinnerService
    ) { }

    ngOnInit(): void {
        const clusterId = this.route.snapshot.data.cluster.id;
        this.topicService.list(clusterId, true).subscribe(topicPage => {
            this.topics = topicPage.content;
        });
        this.refreshGroupId(true);
    }

    resetRecords() {
        this.records = undefined;
        setTimeout(() => {
            this.selectedRecords = [];
            this.records = cloneDeep(this.initRecords);
        }, 100);
    }

    selectTopic(topicName): void {
        const clusterId = this.route.snapshot.data.cluster.id;
        this.consumerGroupService.findByTopic(clusterId, topicName, false).subscribe(consumerGroups => {
            this.consumerGroups = consumerGroups.content;
            if (this.consumerGroups && this.consumerGroups.length > 0) {
                this.form.patchValue({ groupToUse: this.consumerGroups[0].groupId });
            } else {
                this.form.patchValue({ offsetType: 'beginning' });
            }
        });
    }

    refreshGroupId(init: boolean = false): void {
        if (!init || !MessagesComponent.groupId) {
            MessagesComponent.groupId = '' + Math.floor(Math.random() * 100000000);
        }

        // Refresh data
        this.form.patchValue({ groupId: MessagesComponent.groupId });
    }

    copyOffset(): void{
        const clusterId = this.route.snapshot.data.cluster.id;
        const groupId = this.form.controls['groupId'].value as string;
        const groupToUse = this.form.controls['groupToUse'].value as string;
        const addOffset = this.form.controls['addOffset'].value as number;
        this.consumerGroupOffsetService.copyOffset(clusterId, groupId, groupToUse, addOffset).subscribe(() => {
            this.toastr.success(this.translate.instant("messages.editMessage.messages.editOffset.success.text"), this.translate.instant("messages.editMessage.messages.editOffset.success.title"));
            this.editForm.enable();
        }, () => {
            this.toastr.error(this.translate.instant("messages.editMessage.messages.editOffset.error.text"), this.translate.instant("messages.editMessage.messages.editOffset.error.title"));
            this.editForm.enable();
        });
    }

    onSubmit(): void {
        this.spinner.show(this.messagesSpinnerId);

        // Get form data
        const clusterId = this.route.snapshot.data.cluster.id;
        const commit = this.form.controls['commit'].value as boolean;
        const topicName = this.form.controls['topic'].value as string;
        const groupId = this.form.controls['groupId'].value as string;
        const numberOfMessages = this.form.controls['numberOfMessages'].value as number;

        // Call API to get messages
        this.records = undefined;
        MessagesComponent.groupId = groupId;
        this.consumerService.readMessages(clusterId, commit, numberOfMessages, groupId, topicName).subscribe(
            messages => {
                this.records = messages.sort((a,b) => a.offset - b.offset)
                this.initRecords = cloneDeep(this.records);
                this.spinner.hide(this.messagesSpinnerId);
            }
        );

        // Update form depends on the value
        this.editForm.patchValue({
            topic: this.form.controls['topic'].value
        });
    }

    onSubmitEdit(): void {
        this.editForm.disable();

        // Check data
        const topicName = this.editForm.controls.topic.value;
        if (!this.selectedRecords || this.selectedRecords.length < 1) {
            this.toastr.error(this.translate.instant("messages.editMessage.messages.select.error.text"), this.translate.instant("messages.editMessage.messages.select.error.title"));
            this.editForm.enable();
            return ;
        }
        if (!topicName) {
            this.toastr.error(this.translate.instant("messages.editMessage.messages.topicSelect.error.text"), this.translate.instant("messages.editMessage.messages.topicSelect.error.title"));
            this.editForm.enable();
            return ;
        }

        this.confirmCopyModal = true;
    }

    onSubmitEditModal() {
        this.editForm.disable();

        // Map data
        const topicName = this.editForm.controls.topic.value;
        const messages: ProducerMessage[] = this.selectedRecords.map(record => {
            const message = new ProducerMessage();
            // Manage value
            if (typeof record.value === 'object') {
                message.message = JSON.stringify(record.value);
            } else {
                message.message =record.value as string;
            }
            message.key = record.key;
            message.headers = record.headers;
            message.partition = record.partition;
            return message;
        });


        // Call API
        const clusterId = this.route.snapshot.data.cluster.id;
        this.producerService.sendMessages(clusterId, topicName, messages).subscribe(
            () => {
                this.toastr.success(this.translate.instant("messages.editMessage.messages.produce.success.text", {topicName: topicName}), this.translate.instant("messages.editMessage.messages.produce.success.title"));
                this.resetRecords();
                this.editForm.enable();
            }, () => {
                this.toastr.error(this.translate.instant("messages.editMessage.messages.produce.error.text"), this.translate.instant("messages.editMessage.messages.produce.error.title"));
                this.editForm.enable();
            }
        );
    }

    editRecord(record: ConsumerRecord): void {
        this.editedRecord = record;
    }

    updateRecord(data: ConsumerRecord): void {
        this.editedRecord.value = data;
        this.editedRecord = null;
    }

    selectRecord(isCheck, record: ConsumerRecord): void {
        if (isCheck) {
            this.selectedRecords.push(record);
        } else {
            this.selectedRecords = this.selectedRecords.filter(r => r.offset !== record.offset);
        }
    }
}
