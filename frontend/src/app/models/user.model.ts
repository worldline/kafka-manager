export class User {

    id: string
    firstName: string
    lastName: string
    email: string
    fonction: string
    job: string
    creationDate: Date
    company: string
    site: string
    linkedin: string

    constructor(id: string, firstName: string, lastName: string, email: string, fonction: string, job: string, creationDate: Date, company: string, site: string, linkedin: string) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.fonction = fonction
        this.job = job
        this.creationDate = creationDate
        this.company = company
        this.site = site
        this.linkedin = linkedin
    }
}
