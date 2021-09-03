export class ProducerRecord {
    offset: number;
    partition: number;
    timestamp: Date;

    constructor(offset: number, timestamp: Date, partition: number) {
        this.offset = offset;
        this.timestamp = timestamp;
        this.partition = partition;
    }
}
