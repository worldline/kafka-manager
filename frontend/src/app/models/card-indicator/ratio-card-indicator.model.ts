import { CardIndicator } from "./card-indicator.model";
import { SimpleCardIndicator } from "./simple-card-indicator.model";

export class RatioCardIndicator extends SimpleCardIndicator {

    static readonly defaultGaugeColor: string = "green";
    static readonly defaultGaugeWidth: string = "100px";
    static readonly defaultGaugeBackground: string = "#e9ecef";

    constructor(
        title: string, subTitle: string, index: number,
        enableGauge: boolean, gaugeData: string, gaugeRotation: string,
        gaugeColor?: string, gaugeWidth?: string, gaugeBackground?: string,
        bgClasses?: string[], textClasses?: string[], icons?: string[]
    ) {
        super(title, subTitle, index, bgClasses, textClasses, icons);
        this.enableGauge = enableGauge;
        this.gaugeData = gaugeData;
        this.gaugeRotation = gaugeRotation;
        this.gaugeColor = gaugeColor ? gaugeColor : RatioCardIndicator.defaultGaugeColor;
        this.gaugeWidth = gaugeWidth ? gaugeWidth : RatioCardIndicator.defaultGaugeWidth;
        this.gaugeBackground = gaugeBackground ? gaugeBackground : RatioCardIndicator.defaultGaugeBackground;
    }
}
