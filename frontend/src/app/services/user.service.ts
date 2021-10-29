import { Injectable } from '@angular/core'
import { User } from '@models/user.model';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
    providedIn: 'root'
})
/**
* User Service
*/
export class UserService {

    constructor(private jwtHelper: JwtHelperService) {}

    currentUser(): User {

        const token = this.jwtHelper.decodeToken();

        return new User(
            token.id,
            token.firstName,
            token.lastName,
            token.email,
            token.fonction,
            token.job,
            token.creationDate,
            token.company,
            token.site,
            token.linkedin
        )

    }

}
