import { Component, OnInit, AfterViewInit, ViewChild, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ClusterService } from '@services/cluster.service';
import { ToastrService } from 'ngx-toastr';
import { Cluster } from '@models/cluster/cluster.model';
import { GlobalSettingsService } from '@services/global-settings.service';

@Component({
    selector: 'app-menu-sidebar',
    templateUrl: './menu-sidebar.component.html',
    styleUrls: ['./menu-sidebar.component.scss'],
})
export class MenuSidebarComponent implements OnInit, AfterViewInit {
    @ViewChild('mainSidebar', { static: false }) mainSidebar;
    @Output() mainSidebarHeight: EventEmitter<any> = new EventEmitter<any>();

    selectedCluster: Cluster = null;

    constructor(private router: Router,
                private clusterService: ClusterService,
                public settings : GlobalSettingsService,
                private toastr: ToastrService) {}

    ngOnInit() {
        this.clusterService.selectedCluster$.subscribe( cluster => this.selectedCluster = cluster);
    }

    ngAfterViewInit() {
        this.mainSidebarHeight.emit(this.mainSidebar.nativeElement.offsetHeight);
    }

    onRedirectKakfa(category?: string) {
        if(this.selectedCluster) {
            if(category){
                this.router.navigate(['/clusters', this.selectedCluster.id, category]);
            } else {
                this.router.navigate(['/clusters', this.selectedCluster.id]);
            }
        } else {
            this.toastr.error('You must select a cluster before requesting its display.', 'Cluster redirection');
        }
    }
}
