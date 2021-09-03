export class ConsumerRecord {
    key: string;
    offset: number;
    partition: number;
    timestamp: Date;
    value: any;
    headers: any;

    constructor(key: string, offset: number, timestamp: Date, partition: number, value: any, headers: any) {
        this.key = key;
        this.offset = offset;
        this.timestamp = timestamp;
        this.partition = partition;
        this.value = value;
        this.headers = headers;
    }
}
