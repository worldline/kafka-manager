import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClusterUpdate } from '@models/cluster/cluster-update.model';
import { Cluster } from '@models/cluster/cluster.model';
import { ClusterService } from '@services/cluster.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ClusterBrokerAddress } from '@models/cluster/cluster-broker-address.model';
import { GlobalSettingsService } from '@services/global-settings.service';
import * as uuid from 'uuid';

@Component({
    selector: 'app-cluster-detail',
    templateUrl: './cluster-detail.component.html',
    styleUrls: ['./cluster-detail.component.scss'],
})
export class ClusterDetailComponent implements OnInit, OnDestroy {

    cluster: Cluster;
    selectedCluster$: Subscription;
    clusterAddress = [];

    clusterForm = new FormGroup({
        name: new FormControl('', Validators.required),
        kafkaVersion: new FormControl(''),
        zkAddr: new FormControl('', Validators.required),
        kafkaConnectAddr: new FormControl(''),
        comment: new FormControl('')
    });

    constructor(private route: ActivatedRoute,private clusterService: ClusterService,private router: Router,private toastr: ToastrService,public settings: GlobalSettingsService,) { }

    ngOnInit() {
        // Retrieve cluster of resolver/route
        if (this.route.snapshot.data.cluster) {
            this.updateForm(this.route.snapshot.data.cluster);
            this.clusterService.selectedCluster$.subscribe(
                (newCluster) => {
                    if(this.cluster.id !== newCluster.id) {
                        this.cluster = newCluster;
                        this.router.navigate(["/clusters", newCluster.id ]);
                    }
                }
            );
        } else {
            this.addAddress();
        }
    }

    addAddress(server = '', kafka = 9092, jmx = null) {
        const id = uuid.v4();
        this.clusterForm.addControl(`server-${id}`, new FormControl(server, Validators.required))
        this.clusterForm.addControl(`kafka-${id}`, new FormControl(kafka, Validators.required))
        this.clusterForm.addControl(`jmx-${id}`, new FormControl(jmx, Validators.required))
        const addr = new ClusterBrokerAddress(server, kafka, jmx);
        let data = { id: id, data: addr };
        this.clusterAddress.push(data);
    }

    removeAddress(id) {
        this.clusterAddress = this.clusterAddress.filter(item => item.id !== id);
        this.clusterForm.removeControl(`server-${id}`);
        this.clusterForm.removeControl(`kafka-${id}`);
        this.clusterForm.removeControl(`jmx-${id}`);
    }

    ngOnDestroy(): void {
        if(this.selectedCluster$) {
            this.selectedCluster$.unsubscribe();
        }
    }

    updateForm(cluster: Cluster){
        this.cluster = cluster;
        if(this.cluster) {
            this.clusterForm.patchValue({
                name: this.cluster.name,
                kafkaVersion: this.cluster.kafkaVersion,
                zkAddr: this.cluster.zkAddr,
                comment: this.cluster.comment,
                kafkaConnectAddr: this.cluster.kafkaConnectAddr
            });
            this.cluster.brokerAddrs.forEach(addr => {
                this.addAddress(addr.address, addr.kafkaPort, addr.jmxPort);
            });
        } else {
            // No cluster => Creation use case
            this.cluster = new Cluster(undefined, undefined, undefined, undefined, undefined, new Date(), [], undefined);
        }
    }

    onSubmit() {
        this.clusterForm.disable();

        const brokerAddress: ClusterBrokerAddress[] = this.clusterAddress.map(item => {
            item.data.address = this.clusterForm.controls[`server-${item.id}`].value;
            item.data.kafkaPort = this.clusterForm.controls[`kafka-${item.id}`].value;
            item.data.jmxPort = this.clusterForm.controls[`jmx-${item.id}`].value;
            return item.data;
        });

        const clusterInfo = new ClusterUpdate(
            this.clusterForm.controls.name.value,
            this.clusterForm.controls.zkAddr.value,
            this.clusterForm.controls.kafkaVersion.value,
            this.clusterForm.controls.comment.value,
            this.clusterForm.controls.kafkaConnectAddr.value,
            brokerAddress
        );

        if(this.cluster && this.cluster.id) {
            this.clusterService.update(this.cluster.id, clusterInfo).subscribe(data => {
                this.clusterService.refreshClusters();
                this.toastr.success("The cluster " + this.cluster.id + " has been updated", "Cluster Update");
                this.router.navigate(["/clusters", this.cluster.id]);
            }, () => {
                this.toastr.error("Update failed for cluster " + this.cluster.id)
                this.clusterForm.enable();
            });
        } else {
            this.clusterService.create(clusterInfo).subscribe(newCluster => {
                this.toastr.success("The cluster " + newCluster.name + " has been created with id [" + newCluster.id + "]", "Cluster Creation");
                this.clusterService.saveSelectedCluster(newCluster);
                this.clusterService.refreshClusters();
                this.router.navigate(["/"]);
            }, () => {
                this.toastr.error("Creation failed for cluster " + clusterInfo.name)
                this.clusterForm.enable();
            });
        }

    }

    onDelete() {
        this.clusterForm.disable();
        this.clusterService.delete(this.cluster.id).subscribe(data => {
            this.clusterService.saveSelectedCluster(null);
            this.clusterService.refreshClusters();
            this.router.navigate(['/']);
        }, () => {
            this.clusterForm.enable();
        });
    }
}
