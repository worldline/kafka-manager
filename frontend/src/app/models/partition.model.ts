import { ConsumerOffsets } from './consumer-group/consumer-offsets.model';

export class Partition {
    isr: string[];
    leader: number;
    partition: number;
    prefreLeader: boolean;
    replicas: string[];
    underReplicate: boolean;
    name: string;
    offsets: ConsumerOffsets;

    constructor(isr: string[], leader: number, partition: number, prefreLeader: boolean, replicas: string[], underReplicate: boolean, name: string) {
        this.isr = isr;
        this.leader = leader;
        this.partition = partition;
        this.prefreLeader = prefreLeader;
        this.replicas = replicas;
        this.underReplicate = underReplicate;
        this.name = name;
    }
}
