import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConsumerMember } from '@models/consumer-group/consumer-member.model';

@Component({
    selector: 'app-consumer-list',
    templateUrl: './consumer-list.component.html'
})
export class ConsumerListComponent implements OnInit {

    @Input() consumers: ConsumerMember[];
    @Input() displayTopic: boolean = true;
    @Input() displayConsumerGroup: boolean = false;
    @Input() displayOffsets: boolean = true;
    @Input() displayLag: boolean = true;
    @Input() filterTopic: String;
    @Output() computedLag = new EventEmitter<number>();

    consumerList: any = [];
    routeToTopic: string;
    routeToConsumerGroup: string;

    constructor(private route: ActivatedRoute) {}

    ngOnInit(): void {
        // Create routes
        const clusterId = this.route.snapshot.data.cluster.id;
        this.routeToTopic = `/clusters/${clusterId}/topics`;
        this.routeToConsumerGroup = `/clusters/${clusterId}/consumer-groups`;

        // Init data
        if (this.consumers) {
            this.consumerList = this.consumers.filter(consumer => consumer.topicPartitions).map(consumer => {
                return consumer.topicPartitions
                    .filter(partition => !this.filterTopic || partition.name === this.filterTopic)
                    .map(partition => {
                        return {
                            partition: partition.partition,
                            topic: partition.name,
                            id: consumer.consumerId,
                            consumerGroupId: consumer.groupId,
                            host: consumer.host,
                            offsets: partition.offsets
                        };
                    });
            });
            this.consumerList = this.consumerList.flat().sort((a, b) => a.topic.localeCompare(b.topic) || a.partition - b.partition);
            this.computedLag.emit(this.consumerList.map(it => {
                if (it.offsets !== undefined) {
                    return it.offsets.end - it.offsets.current;
                } else {
                    return 0;
                }
            } ).reduce((a, b) => a + b, 0));
        }
    }

}
