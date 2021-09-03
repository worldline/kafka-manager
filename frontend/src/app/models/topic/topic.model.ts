import { Partition } from '@models/partition.model';
import { TopicBroker } from '@models/topic/topic-broker.model';
import { Setting } from '@models/setting/setting.model';

export class Topic {
    brokers: TopicBroker[];
    brokersLeaderSkewed: number;
    brokersSkewed: number;
    brokersSpread: number;
    name: string;
    nbBrokerForTopic: number;
    nbPartitions: number;
    nbReplications: number;
    nbUnderReplication: number;
    partitions: Partition[];
    preferredReplicas: number;
    sumPartitionOffset: number;
    totalNumberOfBrokers: number;
    configuration: Setting[];
}
