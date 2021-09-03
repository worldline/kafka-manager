import { Broker } from "./broker.model"

export class BrokerInfo {

    brokers: Broker[]

    constructor(brokers: Broker[]) {
        this.brokers = brokers
    }

    countBroker(): number {
        return this.brokers.length
    }

    countAvailableBroker(): number {
        return this.brokers.filter(b => "UP" === b.status).length
    }

    getStatus(brokerId: number): string {
        const broker = (this.brokers.find(b => b.id === brokerId))
        return broker ? broker.status : null
    }
}
