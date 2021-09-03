import { ConsumerGroupInfo } from "@models/consumer-group/consumer-group-info.model";
import { SimpleCardIndicator } from "../simple-card-indicator.model";

export class ConsumerGroupLagCardIndicator extends SimpleCardIndicator {

    constructor(data: string) {
        super(
            "consumerGroups.detail.lag",
            data,
            0,
            ConsumerGroupInfo.statusBgClasses,
            ConsumerGroupInfo.statusTextClasses,
            ConsumerGroupInfo.statusIcons
        )
    }

}
