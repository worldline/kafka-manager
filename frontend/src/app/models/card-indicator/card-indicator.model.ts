export class CardIndicator {

    title: string;
    subTitle: string;
    bgClass: string;
    textClass: string;
    icon: string;
    enableGauge: boolean;
    gaugeData: string;
    gaugeRotation: string;
    gaugeColor: string;
    gaugeWidth: string;
    gaugeBackground: string;

    constructor(
        title: string, subTitle: string, bgClass: string, textClass: string, icon: string,
        enableGauge: boolean, gaugeData: string, gaugeRotation: string,
        gaugeColor: string, gaugeWidth: string, gaugeBackground: string
    ) {
        this.title = title;
        this.subTitle = subTitle;
        this.bgClass = bgClass;
        this.textClass = textClass;
        this.icon = icon;
        this.enableGauge = enableGauge;
        this.gaugeData = gaugeData;
        this.gaugeRotation = gaugeRotation;
        this.gaugeColor = gaugeColor;
        this.gaugeWidth = gaugeWidth;
        this.gaugeBackground = gaugeBackground;

    }
}
