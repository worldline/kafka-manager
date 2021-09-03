import { ClusterBrokerAddress } from './cluster-broker-address.model';

export class ClusterUpdate {

    name: string
    zkAddr: string
    kafkaVersion: string
    comment: string
    kafkaConnectAddr: string
    brokerAddrs: ClusterBrokerAddress[]

    constructor(name: string, zkAddr: string, kafkaVersion: string, comment: string, kafkaConnectAddr: string, brokerAddrs: ClusterBrokerAddress[]) {
        this.name = name
        this.zkAddr = zkAddr
        this.kafkaVersion = kafkaVersion
        this.comment = comment
        this.kafkaConnectAddr = kafkaConnectAddr
        this.brokerAddrs = brokerAddrs
    }

}
