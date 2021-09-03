import { ConsumerGroupInfo } from "@models/consumer-group/consumer-group-info.model";
import { SimpleCardIndicator } from "../simple-card-indicator.model";

export class ConsumerGroupStatusCardIndicator extends SimpleCardIndicator {

    constructor(data: string) {
        const index = ConsumerGroupInfo.statusNames.findIndex(n => n.toUpperCase === data.toUpperCase);
        super(
            "consumerGroups.detail.operatingState",
            data,
            index == -1 ? (ConsumerGroupInfo.statusNames.length - 1) : index,
            ConsumerGroupInfo.statusBgClasses,
            ConsumerGroupInfo.statusTextClasses,
            ConsumerGroupInfo.statusIcons
        )
    }

}
