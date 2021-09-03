import { Component, OnDestroy, OnInit } from '@angular/core';
import { Cluster } from '@models/cluster/cluster.model';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';
import { Topic } from '@models/topic/topic.model';
import { TranslateService } from '@ngx-translate/core';
import { ClusterService } from '@services/cluster.service'
import { ConsumerGroupService } from '@services/consumer-group.service';
import { TopicService } from '@services/topic.service';
import { ToastrService } from 'ngx-toastr';
import { GlobalSettingsService } from '@services/global-settings.service';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Subscription } from 'rxjs';
import { CardIndicator } from '@models/card-indicator/card-indicator.model';
import { BrokerService } from '@services/broker.service';
import { EventsService } from '@services/events.service';
import { BrokerSearch } from '@models/broker/broker-search.model';
import { BrokerInfo } from '@models/broker/broker-info.model';
import { SimpleCardIndicator } from '@models/card-indicator/simple-card-indicator.model';
import { NgxSpinnerService } from 'ngx-spinner';
import { Event } from '@models/events/event.model';
import * as moment from 'moment';

@Component({
    selector: 'app-cluster',
    templateUrl: './cluster.component.html',
    styleUrls: ['./cluster.component.scss']
})
export class ClusterComponent implements OnInit, OnDestroy {

    deleteModalState: boolean = false;
    topics: Topic[] = [];
    consumerGroups: ConsumerGroup[] = [];
    brokerInfo: BrokerInfo;
    cluster: Cluster;
    selectedCluster$: Subscription;
    cardIndicators: CardIndicator[];
    initialEvents: any;
    events: any[];
    cardIndicatorSpinnerId: string = 'spinner-card-indicator';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private clusterService: ClusterService,
        private topicService: TopicService,
        private eventService: EventsService,
        private brokerService: BrokerService,
        public settings: GlobalSettingsService,
        private consumerGroupService: ConsumerGroupService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private spinner: NgxSpinnerService
    ) { }

    ngOnInit() {
        // Retrieve cluster of resolver/route
        this.refresh(this.route.snapshot.data.cluster);

        this.selectedCluster$ = this.clusterService.selectedCluster$.subscribe(cluster => {
            this.refresh(cluster);

        });

        this.manageEvents();
    }

    ngOnDestroy(): void {
        if(this.selectedCluster$) {
            this.selectedCluster$.unsubscribe();
        }
    }

    deleteCluster(): void {
        this.clusterService.delete(this.cluster.id);
        this.toastr.info(this.translate.instant('clusters.messages.deletion.text'), this.translate.instant('clusters.messages.deletion.title'));
        this.router.navigate(['/']);
    }

    refresh(cluster: Cluster){
        if(!cluster) {
            this.toastr.error('You must select a cluster before requesting broker display.', 'Broker redirection');
            this.router.navigate(['/']);
        }

        this.cluster = cluster;
        this.spinner.show(this.cardIndicatorSpinnerId);

        forkJoin([
            this.topicService.list(cluster.id),
            this.consumerGroupService.list(cluster.id),
            this.brokerService.list(this.cluster.id, new BrokerSearch(true)),
            this.eventService.listLastMinutes(this.cluster.id, 30)
        ]).subscribe(([topicPage, consumerGroupPage, brokerPage, events]) => {
            this.topics = topicPage.content
            this.consumerGroups = consumerGroupPage.content
            this.brokerInfo = new BrokerInfo(brokerPage.content)

            const pAvailableBroker = this.brokerInfo.countAvailableBroker() / this.brokerInfo.countBroker() * 100;
            const brokerMessage = this.brokerInfo.countAvailableBroker() + " / " + this.brokerInfo.countBroker() + " (" + pAvailableBroker + "% )";
            const brokerValue = (this.brokerInfo.countBroker() == this.brokerInfo.countAvailableBroker()) ? 0 : (this.brokerInfo.countAvailableBroker() == 0) ? 2 : 1

            // TODO add % of topic coverage with available consumer
            const consumerGroupsMessage = this.consumerGroups.length.toString();
            // TODO the current solution is poo, change for it -> check availability consumer per topic
            const consumerGroupsValue = this.consumerGroups.length >= this.topics.length ? 0 : ((this.consumerGroups.length == 0) ? 2 : 1)

            this.cardIndicators = [
                new SimpleCardIndicator("clusters.available.brokers", brokerMessage, brokerValue),
                new SimpleCardIndicator("clusters.available.topics", this.topics.length.toString(), (this.topics.length > 0 ? 0 : 2)),
                new SimpleCardIndicator("clusters.available.consumer-group", consumerGroupsMessage, consumerGroupsValue),
            ]

            // Manage events for timeline
            if (!this.events) {
                this.initialEvents = events;
                this.mapEventData(events);
            }

            this.spinner.hide(this.cardIndicatorSpinnerId);
        }, err => {
            this.spinner.hide(this.cardIndicatorSpinnerId);
            this.toastr.error(this.translate.instant('clusters.messages.error-get-cluster-info.text'), this.translate.instant('clusters.messages.error-get-cluster-info.title'));
        })
    }

    manageEvents() {
        this.translate.onLangChange.subscribe(() => {
            if (this.initialEvents) {
                this.mapEventData(this.initialEvents);
            }
        });
    }

    mapEventData(events) {
        this.events = events.content.map(event => {
            let text = this.translate.instant('timeline.events.' + event.type, event.args);
            if (event.owner) {
                text = event.owner + ' - ' + text;
            }
            return {
                category: '',
                start: moment(event.date).format('YYYY-MM-DD HH:mm:ss'),
                end: moment(event.date).format('YYYY-MM-DD HH:mm:ss'),
                type: event.type,
                text: text,
                color: event.status === 'OK' ? '#149c61' : '#c21b3c',
                icon: 'assets/img/events/' + event.type.toLowerCase().split('_')[0] + '.png'
            };
        });
    }

}
