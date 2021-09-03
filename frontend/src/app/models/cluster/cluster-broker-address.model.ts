export class ClusterBrokerAddress {

  address: string;
  kafkaPort: number;
  jmxPort?: number;

  constructor(address: string, kafkaPort: number, jmxPort: number) {
    this.address = address
    this.kafkaPort = kafkaPort
    this.jmxPort = jmxPort
  }

}