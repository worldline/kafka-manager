import { CardIndicator } from "./card-indicator.model";

export class SimpleCardIndicator extends CardIndicator {

    static readonly statusNames: string[] = ["success", "warning", "danger"]
    static readonly bgClasses: string[] = ["bg-success", "bg-gradient-warning", "bg-danger"]
    static readonly textClasses: string[] = ["SeaGreen", "Gold", "Red"]
    static readonly icons: string[] = ["far fa-thumbs-up", "far fa-thumbs-down", "fas fa-radiation"]

    constructor(title: string, subTitle: string, index: number, bgClasses?:string[], textClasses?:string[], icons?:string[]) {
        super(title, subTitle, '', '', '', false, '', '', '', '', '')

        this.bgClass = (bgClasses && bgClasses.length > 0) ? bgClasses[index] : SimpleCardIndicator.bgClasses[index]
        this.textClass = (textClasses && textClasses.length > 0) ? textClasses[index] : SimpleCardIndicator.textClasses[index]
        this.icon = (icons && icons.length > 0) ? icons[index] : SimpleCardIndicator.icons[index]
    }

}
