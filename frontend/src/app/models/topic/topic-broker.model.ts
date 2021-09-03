export class TopicBroker {
    id: string;
    leaderSkewed: boolean;
    nbLeader: number;
    nbPartitions: number;
    partitions: number[];
    skewed: boolean;

    constructor(id: string, leaderSkewed: boolean, nbLeader: number, nbPartitions: number, partitions: number[], skewed: boolean) {
        this.id = id;
        this.leaderSkewed = leaderSkewed;
        this.nbLeader = nbLeader;
        this.nbPartitions = nbPartitions;
        this.partitions = partitions;
        this.skewed = skewed;
    }
}
