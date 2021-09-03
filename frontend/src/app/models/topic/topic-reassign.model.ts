import {TopicReassignPartition} from './topic-reassign-partition.model';

export class TopicReassign {

  partitions: TopicReassignPartition[];

  constructor(partitions: TopicReassignPartition[]) {
    this.partitions = partitions;
  }

}
