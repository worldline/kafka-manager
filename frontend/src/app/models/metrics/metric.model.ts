import { MetricOffset } from './metric-offset.model'

export class Metric {

	date: Date;
  clusterId: string;
	topicName: string;
	messagesPerSeconds: number;
	bytesInPerSeconds: number;
	bytesOutPerSeconds: number;
	bytesRejectedPerSeconds: number;
	failedFetchPerSeconds: number;
	failedProducePerSeconds: number;
	offsets: MetricOffset[];

}
