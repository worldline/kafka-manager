import { ConsumerGroupOffsetPartitionReset } from './consumer-group-offset-partition-reset.model';

export class ConsumerGroupOffsetReset {
  
  offsets: ConsumerGroupOffsetPartitionReset[];

  constructor(offsets: ConsumerGroupOffsetPartitionReset[]) {
    this.offsets = offsets;
  }

}