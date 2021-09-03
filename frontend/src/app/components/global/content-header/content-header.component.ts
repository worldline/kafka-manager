import { Component } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Event, NavigationEnd, Router } from '@angular/router';
import { distinctUntilChanged, filter } from 'rxjs/operators';

@Component({
  selector: 'app-content-header',
  templateUrl: './content-header.component.html',
})
export class ContentHeaderComponent {

  data: any = {};

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.data = this.buildTitlePage(this.activatedRoute.snapshot.root);
  }

  ngOnInit() {
    this.router.events.pipe(
      filter((event: Event) => event instanceof NavigationEnd),
      distinctUntilChanged(),
    ).subscribe(() => {
        this.data = this.buildTitlePage(this.activatedRoute.snapshot.root);
    })
  }

  buildTitlePage(route: ActivatedRouteSnapshot, title: string= '', params : {} = {}) {

    //If no routeConfig is avalailable we are on the root path
    let currentTitle = route.routeConfig && route.routeConfig.data ? route.routeConfig.data.title : '';
    let currentPath = route.routeConfig && route.routeConfig.path ? route.routeConfig.path : '';
    let currentParams = route.routeConfig ? route.params[route.routeConfig.path] : {};

    if(route.firstChild) {
        return this.buildTitlePage(route.firstChild, currentTitle, currentParams);
    }

    if(currentTitle) {
      title = currentTitle;
      params = currentParams
    }

    return { 'title' : title, 'params' : route.params }
  }

}
