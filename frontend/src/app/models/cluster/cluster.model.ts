import { ClusterBrokerAddress } from './cluster-broker-address.model';

export class Cluster {

    id: string
    name: string
    zkAddr: string
    kafkaVersion: string
    comment?: string
    creationDate: Date
    brokerAddrs: ClusterBrokerAddress[]
    kafkaConnectAddr: string

    constructor(id: string, name: string, zkAddr: string, kafkaVersion: string, comment: string, creationDate: Date, brokerAddrs: ClusterBrokerAddress[], kafkaConnectAddr: string) {
        this.id = id
        this.name = name
        this.zkAddr = zkAddr
        this.kafkaVersion = kafkaVersion
        this.comment = comment
        this.creationDate = creationDate
        this.brokerAddrs = brokerAddrs
        this.kafkaConnectAddr = kafkaConnectAddr
    }
}
