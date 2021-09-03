import { Component, OnInit, ViewChild } from '@angular/core';
import { Cluster } from '@models/cluster/cluster.model';
import { Router } from '@angular/router';
import { ConsumerGroup } from '@models/consumer-group/consumer-group.model';
import { Topic } from '@models/topic/topic.model';
import { TranslateService } from '@ngx-translate/core';
import { ClusterService } from '@services/cluster.service'
import { ToastrService } from 'ngx-toastr';
import { GlobalSettingsService } from '@services/global-settings.service';

@Component({
    selector: 'app-clusters',
    templateUrl: './clusters.component.html',
    styleUrls: ['./clusters.component.scss'],
})
export class ClustersComponent implements OnInit {

    clusters: Cluster[] = [];
    topics: Topic[] = [];
    consumerGroups: ConsumerGroup[] = [];
    nbBrokers = 0;

    constructor(
        private clusterService: ClusterService,
        public settings: GlobalSettingsService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        // Manage case of restricted, redirect to the cluster dashboard
        if (!this.settings.enableDatabase) {
            this.clusterService.Clusters$.subscribe(clusters => {
                if (clusters && clusters.length > 0) {
                    this.router.navigate(["/clusters", clusters[0].id ]);
                }
            });
        } else {
            this.clusterService.Clusters$.subscribe(clusters => {
                this.clusters = clusters ? clusters : [];
            });
        }
    }

    onSelectCluster(cluster: Cluster) {
        this.clusterService.saveSelectedCluster(cluster);
        this.toastr.success(this.translate.instant('clusters.messages.selection.text', { clusterName: cluster.name }), this.translate.instant('clusters.messages.selection.title'));
    }

}
