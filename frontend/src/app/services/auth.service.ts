import { HttpClient, HttpHeaders, HttpParams} from '@angular/common/http'
import { Injectable } from '@angular/core'
import { BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
    providedIn: 'root'
})
/**
* Service Auth
*/
export class AuthService {

    loginUrl = "/login"

    // store the URL so we can redirect after logging in
    redirectUrl: string;

    constructor(private httpClient: HttpClient, private jwtHelper: JwtHelperService) {
        // In case where an existing token is present, we load roles
        if(this.isAuthenticated()) {
            this.loadRoles()
        }
    }

    // A stream that exposes all the roles a user has
    roles$ = new BehaviorSubject<string[]>([]);

    // We leverage this roleUpdates$ to be able to update the roles
    // This is for demonstration purposes only
    roleUpdates$ = new BehaviorSubject(['basic']);

    isAuthenticated(): boolean {
        if(this.jwtHelper.isTokenExpired()){
            this.logout()
            return false
        }
        return true
    }

    login(username: string, password: string): Observable<any> {
        const body = new HttpParams()
            .set('username', username)
            .set('password', password);

        const headers= new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded')
            .set('responseType', 'text');

        return this.httpClient.post<any>('/api/login', body, {headers: headers})
            .pipe(
                tap(response => {
                    localStorage.setItem("token", response.token)
                    this.loadRoles()
                })
            )
    }

    loadRoles() {
        this.roles$.next(this.jwtHelper.decodeToken().roles)
    }

    logout(): void {
        localStorage.removeItem("token")
    }
}
