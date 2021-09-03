import { DatePipe } from "@angular/common";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class HelperService {

    constructor(public datepipe: DatePipe) {}

    convertDate(date: Date) : string {
        return this.datepipe.transform(date, 'dd/MM/yyyy');
    }

    convertDateTime(date: Date) : string {
        return this.datepipe.transform(date, 'dd/MM/yyyy HH:mm:ss');
    }
}
