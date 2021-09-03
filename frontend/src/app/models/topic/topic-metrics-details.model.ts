export class TopicMetricsDetails {
    fifteenMinuteRate: number;
    fiveMinuteRate: number;
    meanRate: number;
    oneMinuteRate: number;

    constructor(fifteenMinuteRate: number, fiveMinuteRate: number, meanRate: number, oneMinuteRate: number) {
        this.fifteenMinuteRate = fifteenMinuteRate;
        this.fiveMinuteRate = fiveMinuteRate;
        this.meanRate = meanRate;
        this.oneMinuteRate = oneMinuteRate;
    }
}
