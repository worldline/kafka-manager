import { Component, OnInit } from '@angular/core';
import { ConnectService } from '@services/connect.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Connector } from '@models/connect/connector.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-connect-dashboard',
    templateUrl: './dashboard.component.html'
})
export class ConnectDashboardComponent implements OnInit {

    sinkConnectors: number = 0;
    sourceConnectors: number = 0;
    topicsNumber: number = 0;
    connectors: Connector[];

    clusterId: string;
    routeToTopic: string;
    events: Object[] = [];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private connectService: ConnectService,
        private toastr: ToastrService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.routeToTopic = `/clusters/${this.clusterId}/topics`;
        this.connectService.connectors(this.clusterId).subscribe(connectors => {
            const topics = [];
            this.connectors = connectors.content.sort((a,b) => a.name.localeCompare(b.name))
            this.connectors.forEach(connector => {
                // Manage indicators
                let topic = this.getTopic(connector);
                if (connector.type === 'sink') {
                    topic = connector.config['topics'];
                    this.sinkConnectors++;
                    this.events.push(this.createEvent(topic, connector.name));
                } else {
                    topic = connector.config['topic'];
                    this.sourceConnectors++;
                    this.events.push(this.createEvent(connector.name, topic));
                }
                if (!topics.includes(topic)) {
                    topics.push(topic);
                }
            });
            this.topicsNumber = topics.length;
        }, () => {
            this.toastr.error(this.translate.instant(`connect.dashboard.messages.error.text`), this.translate.instant(`connect.dashboard.messages.error.title`));
        });
    }

    createEvent(from: string, to: string, value: number = 1) {
        return {
            from: from,
            to: to,
            value: value
        };
    }

    goToCreateConnector() {
        this.router.navigateByUrl(`/clusters/${this.clusterId}/connect/add-connector`);
    }

    getTopic(connector) {
        const config = connector.config;
        const found = Object.keys(config).find(c => {
            return c.split('.').some(d => d.includes('topic'));
        });
        return found ? config[found] : null;
    }

}
