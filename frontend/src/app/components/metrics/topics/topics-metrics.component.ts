import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MetricsService } from '@services/metrics.service';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Metric } from '@models/metrics/metric.model'
import * as moment from 'moment';
import * as orderBy from 'lodash/orderBy'

@Component({
    selector: 'app-topics-metrics',
    templateUrl: './topics-metrics.component.html'
})
export class TopicsMetricsComponent implements OnInit {

    private clusterId: string;
    private data: Metric[];

    heatData: any;

    constructor(private route: ActivatedRoute,
                private metricsService: MetricsService,
                private router: Router,
                private toastr: ToastrService,
                private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.metricsService.listLastMinutes(this.clusterId, 30).subscribe(data => {
            this.data = data.content;
            this.computeHeatData(data.content);
        });
    }

    computeHeatData(data: Metric[]) {
        const result = data.filter(d => d.offsets != null).map(item => {
            let lag = 0;
            item.offsets.filter(offset => offset.currentOffset >= 0).forEach(offset => {
                lag += (offset.endOffset - offset.currentOffset);
            });
            return {
                topic: item.topicName,
                date: moment(item.date).format("HH:mm"),
                value: lag
            };
        });
        this.heatData = {
            headers: {
                x: {
                    column: 'date',
                    label: 'date'
                },
                y: {
                    column: 'topic',
                    label: 'topic'
                }
            },
            data: orderBy(result, ['date', 'topic'], ['asc', 'asc'])
        };
    }

    goToTopic(event) {
        this.router.navigateByUrl(`/clusters/${this.clusterId}/topics/${event.topic}`);
    }

}
