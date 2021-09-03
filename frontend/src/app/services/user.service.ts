import { Injectable } from '@angular/core'
import { ToastrService } from 'ngx-toastr';
import { User } from '@models/user.model';
import { JwtHelperService } from '@auth0/angular-jwt';
import { componentFactoryName } from '@angular/compiler';

@Injectable({
    providedIn: 'root'
})
/**
* User Service
*/
export class UserService {

    BCD: User = new User("A525125", "Baptiste", "COULAUD", "bpatiste.coulaud@worldline.com", "Expert", "Architect Strategist", new Date("07/10/2013"), "Worldline", "Villeurbanne", "https://www.linkedin.com/in/baptiste-coulaud/");
    EHT: User = new User("A705179", "Elliot", "HUMBERT", "elliot.humbert@worldline.com", "Junior Developer", "Back & Front Strategist", new Date("09/09/2019"), "Worldline", "Villeurbanne", "https://www.linkedin.com/in/elliothumbert/");
    FFN: User = new User("A772133", "Flavien", "FLANDRIN", "flavin.flandrin@worldline.com", "Senior Developer", "Back Strategist", new Date("04/11/2019"), "Worldline", "Villeurbanne", "https://www.linkedin.com/in/flavien-flandrin-97907716/");
    RTY: User = new User("A132242", "Romain", "THELY", "romain.thely@worldline.com", "Expert", "Deployment Strategist", new Date("12/01/2006"), "Worldline", "Villeurbanne", "https://www.linkedin.com/in/romain-thely/");

    contactsUser: User[] = [ this.BCD, this.EHT, this.FFN, this.RTY ];

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
