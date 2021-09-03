export class ConsumerGroupOffsetPartitionReset {
  
  topic: string;
  partition: number;
  offset: number;

  constructor(topic: string, partition: number, offset: number) {
    this.topic = topic;
    this.partition = partition;
    this.offset = offset;
  }

}