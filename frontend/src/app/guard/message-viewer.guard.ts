import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { GlobalSettingsService } from '@services/global-settings.service';

@Injectable({
    providedIn: 'root'
})
export class MessageViewerGuard implements CanActivate {

    constructor(private settings : GlobalSettingsService,
                private authService: AuthService,
                private router: Router) {}

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): true|UrlTree {
        const url: string = state.url;
        return this.checkLogin(url);
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): true|UrlTree {
        return this.canActivate(route, state);
    }

    checkLogin(url: string): true|UrlTree {
        if (this.settings.enableMessageViewer) { return true; }

        // Store the attempted URL for redirecting
        this.authService.redirectUrl = url;

        // Redirect to the login page
        return this.router.parseUrl(this.authService.loginUrl);
      }

}
