import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Event, NavigationEnd, Router } from '@angular/router';
import { IBreadCrumb } from 'app/interface/breadcrumb.interface';
import { distinctUntilChanged, filter } from 'rxjs/operators';

@Component({
    selector: 'app-breadcrumb',
    templateUrl: './breadcrumb.component.html',
    styleUrls: ['./breadcrumb.component.scss'],
})
export class BreadcrumbComponent implements OnInit {

    public breadcrumbs: IBreadCrumb[];

    constructor(private router: Router, private activatedRoute: ActivatedRoute) {
        this.breadcrumbs = this.buildBreadCrumb(this.activatedRoute.snapshot.root);
    }

    ngOnInit() {
        this.router.events.pipe(
            filter((event: Event) => event instanceof NavigationEnd),
            distinctUntilChanged(),
        ).subscribe(() => {
            this.breadcrumbs = this.buildBreadCrumb(this.activatedRoute.snapshot.root);
        })
    }

    /**
    * Recursively build breadcrumb according to activated route.
    * @param route
    * @param url
    * @param breadcrumbs
    */
    buildBreadCrumb(route: ActivatedRouteSnapshot, url: string = '', breadcrumbs: IBreadCrumb[] = []): IBreadCrumb[] {

        breadcrumbs = (!breadcrumbs || breadcrumbs.length < 1) ? [ {label: 'breadcrumb.home', url: '/'} ] : [ ...breadcrumbs];

        //If no routeConfig is avalailable we are on the root path
        let label = route.routeConfig && route.routeConfig.data ? route.routeConfig.data.breadcrumb : '';
        let path = route.routeConfig ? route.routeConfig.path : '';
        ​
        // If the route is dynamic route such as ':id', remove it
        const lastRoutePart = path.split('/').pop();
        if(lastRoutePart.startsWith(':')) {
            const paramName = lastRoutePart.split(':')[1];
            const paramValue : string = route.params[paramName];

            path = path.replace(lastRoutePart, paramValue);
            // Add exception for cluster
            if(label !== "") {
                label = paramValue;
            }
        }

        //In the routeConfig the complete path is not available, so we rebuild it each time
        const nextUrl = path ? `${url}/${path}` : url;

        // Only adding route with non-empty label
        if(label) {
            breadcrumbs.push({ label: label, url: nextUrl });
        }

        if (route.firstChild) {
            //If we are not on our current path yet,
            //there will be more children to look after, to build our breadcumb
            return this.buildBreadCrumb(route.firstChild, nextUrl, breadcrumbs);
        }

        return breadcrumbs;
    }
}
