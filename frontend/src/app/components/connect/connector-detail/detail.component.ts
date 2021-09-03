import { Component, OnInit } from '@angular/core';
import { ConnectService } from '@services/connect.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Connector } from '@models/connect/connector.model';
import { ConnectorTask } from '@models/connect/connector-task.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs'
import { SimpleCardIndicator } from "@models/card-indicator/simple-card-indicator.model";

@Component({
    selector: 'app-connect-detail-connector',
    templateUrl: './detail.component.html',
    styleUrls: ['./detail.component.scss'],
})
export class ConnectDetailConnectorComponent implements OnInit {

    connector: Connector;
    private clusterId: string;
    deleteModalState: boolean = false;
    traceModalState: boolean = false;
    cardIndicator: SimpleCardIndicator;
    selectedTask: ConnectorTask;
    routeToTopic: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private connectService: ConnectService,
        private toastr: ToastrService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        this.clusterId = this.route.snapshot.data.cluster.id;
        this.connector = this.route.snapshot.data.connector;
        this.routeToTopic = `/clusters/${this.clusterId}/topics`;
        this.cardIndicator = new SimpleCardIndicator("connect.connector.indicators.status", this.connector.connector['state'], this.connector.status === 'UP' ? 0 : 2);
    }

    pauseConnector() {
        this.actionConnector(this.connectService.pauseConnector(this.clusterId, this.connector.name), 'pause');
    }

    resumeConnector() {
        this.actionConnector(this.connectService.resumeConnector(this.clusterId, this.connector.name), 'resume');
    }

    restartConnector() {
        this.actionConnector(this.connectService.restartConnector(this.clusterId, this.connector.name), 'restart');
    }

    deleteConnector() {
        this.actionConnector(this.connectService.deleteConnector(this.clusterId, this.connector.name), 'delete', true);
    }

    actionConnector(response: Observable<void>, type, toDashboard: boolean = false) {
        response.subscribe(
            () => {
                this.toastr.success(this.translate.instant(`connect.connector.messages.${type}.success.text`, {connectorName: this.connector.name}), this.translate.instant(`connect.connector.messages.${type}.success.title`));
                if (toDashboard) {
                    this.router.navigateByUrl(`/clusters/${this.clusterId}/connect`);
                } else {
                    this.router.navigateByUrl(`/clusters/${this.clusterId}/connect/${this.connector.name}`);
                }
            }, () => {
                this.toastr.error(this.translate.instant(`connect.connector.messages.${type}.error.text`, {connectorName: this.connector.name}), this.translate.instant(`connect.connector.messages.${type}.error.title`));
            }
        );
    }

    goToEditConnector() {
        this.router.navigateByUrl(`/clusters/${this.route.snapshot.data.cluster.id}/connect/${this.connector.name}/edit`);
    }

    restartTask(task: string) {
        this.actionConnector(this.connectService.restartTask(this.clusterId, this.connector.name, task), 'restartTask');
    }

    showTrace(task: ConnectorTask) {
        this.selectedTask = task;
        this.traceModalState = true;
    }

}