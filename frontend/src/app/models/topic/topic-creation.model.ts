import {Setting} from '@models/setting/setting.model';

export class TopicCreation {
    name: string;
    nbPartitions: number;
    replicationFactor: number;
    configuration: Setting[];

    constructor(name: string, nbPartitions: number, replicationFactor: number, retentionTime: number, retentionBytes: number) {
        this.name = name;
        this.nbPartitions = nbPartitions;
        this.replicationFactor = replicationFactor;
        this.configuration = [];
        if (retentionTime && retentionTime > 0) {
            this.configuration.push(new Setting('retention.ms', retentionTime.toString()));
        }
        if (retentionBytes && retentionBytes > 0) {
            this.configuration.push(new Setting('retention.bytes', retentionBytes.toString()))
        }
    }

}
