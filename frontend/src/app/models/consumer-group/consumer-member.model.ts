import { Partition } from '../partition.model';

export class ConsumerMember {

  consumerId: string;
	groupInstanceId: string;
	clientId: string;
	host: string;
	topicPartitions: Partition[];
  groupId: string;

  constructor(consumerId: string, groupInstanceId: string, clientId: string, host: string, topicPartitions: Partition[], groupId: string) {
    this.consumerId = consumerId;
    this.groupInstanceId = groupInstanceId;
    this.clientId = clientId;
    this.host = host;
    this.topicPartitions = topicPartitions;
    this.groupId = groupId;
  }

}