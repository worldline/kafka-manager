import { ConsumerGroupInfo } from "@models/consumer-group/consumer-group-info.model";
import { SimpleCardIndicator } from "../simple-card-indicator.model";

export class ConsumerGroupTypeCardIndicator extends SimpleCardIndicator {

    constructor(data: boolean) {

        const simpleConsumerMessage = "consumerGroups.list.table.columns.simpleConsumerGroup." + (data ? "simple" : "highLevel")

        super(
            "consumerGroups.detail.type",
            simpleConsumerMessage,
            -1,
            ConsumerGroupInfo.typeBgClasses,
            ConsumerGroupInfo.typeTextClasses,
            ConsumerGroupInfo.typeIcons
        )
    }

}
