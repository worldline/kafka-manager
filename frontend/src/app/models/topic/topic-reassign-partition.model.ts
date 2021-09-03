export class TopicReassignPartition {

  partitionId: number;
  replicas: number[];

  constructor(partitionId: number, replicas: number[]) {
    this.partitionId = partitionId;
    this.replicas = replicas;
  }

}
