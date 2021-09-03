import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { BrokerSearch } from '@models/broker/broker-search.model';
import { Broker } from '@models/broker/broker.model';
import { Cluster } from '@models/cluster/cluster.model';
import { BrokerService } from '@services/broker.service';
import { ClusterService } from '@services/cluster.service';
import { Pageable } from "@models/pageable.model";

@Component({
    selector: 'app-brokers',
    templateUrl: './brokers.component.html',
    styleUrls: ['./brokers.component.css']
})
export class BrokersComponent implements OnInit, OnDestroy {

    cluster: Cluster;
    fulldetail: boolean = true;
    brokers: Broker[] = [];
    pageable: Pageable = new Pageable();

    selectedCluster$: Subscription;

    constructor(
        private route: ActivatedRoute,
        private clusterService: ClusterService,
        private brokerService: BrokerService,
        private router: Router,
        private toastr: ToastrService
    ) { }

    ngOnInit(): void {
        // Retrieve cluster of resolver/route
        this.cluster = this.route.snapshot.data.cluster;
        if(!this.cluster) {
            this.toastr.error('You must select a cluster before requesting broker display.', 'Broker redirection');
            this.router.navigate(['/']);
        }

        this.onRefresh();

        this.selectedCluster$ = this.clusterService.selectedCluster$.subscribe(
            (newCluster) => {
                if(this.cluster.id !== newCluster.id) {
                    this.cluster = newCluster;
                    this.router.navigate(["/clusters", newCluster.id , "brokers"]);
                }
            }
        );

    }

    ngOnDestroy(): void {
        if(this.selectedCluster$) {
            this.selectedCluster$.unsubscribe();
        }
    }

    onRefresh() {
        this.brokerService.list(this.cluster.id, new BrokerSearch(this.fulldetail)).subscribe(
            data => {
                this.brokers = data.content;
                this.pageable = Pageable.readPage(data);
            },
            () => this.toastr.error("Error during borker list of Cluster " + this.cluster.name)
        );
    }

    pageChange(newPage: number) {
        this.pageable.currentPage = newPage;
        this.router.navigate([`/clusters/${this.cluster.id}/brokers`], { queryParams: { page: newPage } });
    }
}
