import { ConsumerGroupInfo } from "@models/consumer-group/consumer-group-info.model";
import { SimpleCardIndicator } from "../simple-card-indicator.model";

export class ConsumerGroupPartitionAssignorCardIndicator extends SimpleCardIndicator {

    constructor(data: string) {
        const index = ConsumerGroupInfo.partitionAssiagnorNames.findIndex(n => n.toUpperCase === data.toUpperCase);
        super(
            "consumerGroups.detail.partitionAssignor",
            data,
            index == -1 ? (ConsumerGroupInfo.partitionAssiagnorNames.length - 1) : index,
            ConsumerGroupInfo.partitionAssiagnorBgClasses,
            ConsumerGroupInfo.partitionAssiagnorTextClasses,
            ConsumerGroupInfo.partitionAssiagnorIcons
        )
    }

}
