import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MetricsService } from '@services/metrics.service';
import { TopicMetrics } from '@models/topic/topic-metrics.model';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { MetricOffset } from '@models/metrics/metric-offset.model'
import { Metric } from '@models/metrics/metric.model'
import { interval, Subscription } from 'rxjs';

@Component({
    selector: 'app-topic-metrics',
    templateUrl: './topic-metrics.component.html',
    styleUrls: ['./topic-metrics.component.scss']
})
export class TopicMetricsComponent implements OnInit {

    private clusterId: string;
    private data: Metric[];

    chartData: any;
    lagData: any;

    jmxEnabled: boolean;
    topicMetrics: TopicMetrics;
    lagMetrics: MetricOffset[];

    routeToConsumerGroup: string;

    // Reload Data
    subscription: Subscription;
    reloadInterval: number = 60000;

    constructor(private route: ActivatedRoute,
        private metricsService: MetricsService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService)
    {
        // Enable auto reload metrics and charts
        this.subscription = interval(this.reloadInterval).subscribe((val) => {
            this.loadingChartsData();
            this.loadingMetrics();
        });
    }

    ngOnInit(): void {
        // Define route
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.routeToConsumerGroup = `/clusters/${this.clusterId}/consumer-groups`;

        // Load charts data
        this.loadingChartsData();

        // Load metrics
        this.loadingMetrics();
    }

    ngOnDestroy() {
        // Disbale auto reload metrics and charts
        this.subscription.unsubscribe();
    }

    loadingMetrics() {
        // Metrics require a JMX connection
        this.jmxEnabled = !!this.route.snapshot.data.cluster.brokerAddrs.some(d => d.jmxPort);

        if (this.jmxEnabled) {
            const topic = this.route.snapshot.data.topic;
            this.metricsService.topicMetric(this.route.snapshot.data.cluster.id, topic.name).subscribe(
                metrics => this.topicMetrics = metrics,
                error => this.toastr.error(this.translate.instant('metrics.topic.currentMetrics.messages.error.get.text'), this.translate.instant('metrics.topic.currentMetrics.messages.error.get.title'))
            );
        }
    }

    loadingChartsData() {
        const topicName = this.route.snapshot.data.topic.name;
        this.metricsService.listLastMinutes(this.clusterId, 30, topicName).subscribe(data => {
            this.data = data.content;
            this.computeChartData(data.content);
            this.computeLagData(data.content);
        });
    }

    computeChartData(data) {
        this.chartData = {
            headers: [{
                column: 'bytesInPerSeconds',
                label: 'metrics.topic.charts.bytesInPerSeconds'
            }, {
                column: 'bytesOutPerSeconds',
                label: 'metrics.topic.charts.bytesOutPerSeconds'
            }, {
                column: 'messagesPerSeconds',
                label: 'metrics.topic.charts.messagesPerSeconds'
            }, {
                column: 'bytesRejectedPerSeconds',
                label: 'metrics.topic.charts.bytesRejectedPerSeconds'
            }, {
                column: 'failedFetchPerSeconds',
                label: 'metrics.topic.charts.failedFetchPerSeconds'
            }, {
                column: 'failedProducePerSeconds',
                label: 'metrics.topic.charts.failedProducePerSeconds'
            }],
            data: data
        };
    }

    computeLagData(data: Metric[], byPartition: boolean = false) {
        // Compute chart
        const groups = [];
        const lagData = data.filter(d => d.offsets != null).map(item => {
            let result = {
                date: item.date,
            };
            item.offsets.forEach(offset => {
                let key = offset.groupId;
                if (byPartition) {
                    key += '-' + offset.partition;
                }
                if (!groups.includes(key)) {
                    groups.push(key);
                }
                let lag = 0;
                if (offset.currentOffset >= 0) {
                    lag = offset.endOffset - offset.currentOffset;
                }
                result[key] = ((result[key]) ? result[key] : 0) + lag;
            });
            return result;
        });
        const headers = groups.map(key => {
            return {
                column: key,
                label: key
            };
        });
        this.lagData = {
            headers: headers,
            data: lagData
        };

        // Compute lag
        if (data.length > 0) {
            this.lagMetrics = data[data.length - 1].offsets;
        }
    }

    byPartition(event) {
        const value = event.currentTarget.checked;
        this.computeLagData(this.data, value);
    }

    toggleTabs(event) {
        // Get current name
        const tabName = event.srcElement.getAttribute('data-tab');

        // Manage activate buttons
        const buttons = event.srcElement.parentNode.parentNode.getElementsByTagName('a');
        for (let i = 0; i < buttons.length; i++) {
            const input = buttons[i];
            const currentTabName = input.getAttribute('data-tab');
            const isCurrentTab = tabName === currentTabName;
            if (isCurrentTab) {
                input.setAttribute('class', input.getAttribute('class') + ' active');
            } else {
                input.classList.remove('active');
            }
        }

        // Manage activate div
        const divs = event.srcElement.parentNode.parentNode.parentNode.parentNode.getElementsByClassName('card-body')[0].querySelectorAll('[data-tab]');
        for (let i = 0; i < divs.length; i++) {
            const input = divs[i];
            const currentTabName = input.getAttribute('data-tab');
            const isCurrentTab = tabName === currentTabName;
            if (isCurrentTab) {
                input.classList.remove('d-none');
            } else {
                input.setAttribute('class', input.getAttribute('class') + ' d-none');
            }
        }

    }

}
