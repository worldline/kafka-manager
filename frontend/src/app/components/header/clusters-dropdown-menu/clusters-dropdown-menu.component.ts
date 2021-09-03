import { Component, OnInit, ViewChild, HostListener, ElementRef, Renderer2, Output, EventEmitter, Input } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Cluster } from '@models/cluster/cluster.model';
import { ClusterService } from '@services/cluster.service';
import { GlobalSettingsService } from '@services/global-settings.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-clusters-dropdown-menu',
    templateUrl: './clusters-dropdown-menu.component.html',
    styleUrls: ['./clusters-dropdown-menu.component.scss'],
})
export class ClustersDropdownMenuComponent implements OnInit {

    @ViewChild('dropdownMenu', { static: false }) dropdownMenu;

    selectedCluster: Cluster;
    clusters: Cluster[] = [];

    @HostListener('document:click', ['$event'])
    clickout(event) {
        if (!this.elementRef.nativeElement.contains(event.target)) {
            this.hideDropdownMenu();
        }
    }

    constructor(
        private elementRef: ElementRef,
        private renderer: Renderer2,
        public clusterService : ClusterService,
        public settings : GlobalSettingsService,
        private router: Router,
        private route: ActivatedRoute,
        private toastr: ToastrService) {

            this.router.routeReuseStrategy.shouldReuseRoute = () => {
                return false;
            }

            this.router.events.subscribe((evt) => {
                if (evt instanceof NavigationEnd) {
                    this.router.navigated = false;
                }
            });
        }

        ngOnInit() {
            this.clusterService.selectedCluster$.subscribe(selectedCluster => {
                this.selectedCluster = selectedCluster;
            });

            this.clusterService.Clusters$.subscribe(clusters => {
                this.clusters = clusters ? clusters : [];

                if(!clusters) {
                    this.onRefresh();
                } else if (clusters.length === 1) {
                    if(!this.selectedCluster && this.clusters.length == 1) {
                        this.clusterService.saveSelectedCluster(this.clusters[0]);
                    }
                }
            });

        }

        onRefresh(): void {
            this.clusterService.refreshClusters()
        }

        onSelect(cluster: Cluster): void {
            this.clusterService.saveSelectedCluster(cluster);
        }

        toggleDropdownMenu() {
            if (this.dropdownMenu.nativeElement.classList.contains('show')) {
                this.hideDropdownMenu();
            } else {
                this.showDropdownMenu();
            }
        }

        showDropdownMenu() {
            this.renderer.addClass(this.dropdownMenu.nativeElement, 'show');
        }

        hideDropdownMenu() {
            this.renderer.removeClass(this.dropdownMenu.nativeElement, 'show');
        }

    }
