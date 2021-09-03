import {TopicMetricsDetails} from './topic-metrics-details.model';

export class TopicMetrics {
    bytesInPerSeconds: TopicMetricsDetails;
    bytesOutPerSeconds: TopicMetricsDetails;
    bytesRejectedPerSeconds: TopicMetricsDetails;
    failedFetchPerSeconds: TopicMetricsDetails;
    failedProducePerSeconds: TopicMetricsDetails;
    messagesPerSeconds: TopicMetricsDetails;

    constructor(bytesInPerSeconds: TopicMetricsDetails, bytesOutPerSeconds: TopicMetricsDetails, bytesRejectedPerSeconds: TopicMetricsDetails, failedFetchPerSeconds: TopicMetricsDetails, failedProducePerSeconds: TopicMetricsDetails, messagesPerSeconds: TopicMetricsDetails) {
        this.bytesInPerSeconds = bytesInPerSeconds;
        this.bytesOutPerSeconds = bytesOutPerSeconds;
        this.bytesRejectedPerSeconds = bytesRejectedPerSeconds;
        this.failedFetchPerSeconds = failedFetchPerSeconds;
        this.failedProducePerSeconds = failedProducePerSeconds;
        this.messagesPerSeconds = messagesPerSeconds;
    }
}
