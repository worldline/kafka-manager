import { Setting } from '@models/setting/setting.model';

export class Broker {

    id: number
    host: string
    port: number
    nbTopics: number
    nbPartitions: number
    nbLeaderPartitions: number
    status: string
    configuration: Setting[];

    constructor(id: number, host: string, port: number, nbTopics: number, nbPartitions: number, nbLeaderPartitions: number, status: string) {
        this.id = id
        this.host = host
        this.port = port
        this.nbTopics = nbTopics
        this.nbPartitions = nbPartitions
        this.nbLeaderPartitions = nbLeaderPartitions
        this.status = status
    }
}
