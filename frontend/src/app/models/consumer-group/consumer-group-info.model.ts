export class ConsumerGroupInfo {

    static readonly statusNames: string[] = ["Stable", "PreparingRebalance", "CompletingRebalance", "AwaitingSync", "Empty", "Dead"]
    static readonly statusBgClasses: string[] = ["bg-success", "bg-gradient-warning", "bg-gradient-warning", "bg-gradient-warning", "bg-danger", "bg-danger"]
    static readonly statusTextClasses: string[] = ["text-success", "text-warning", "text-warning", "text-warning", "text-danger", "text-danger"]
    static readonly statusIcons: string[] = ["far fa-thumbs-up", "fas fa-sync-alt", "far fa-pause-circle", "far fa-pause-circle", "fas fa-battery-empty", "far fa-dizzy"]

    static readonly partitionAssiagnorNames: string[] = ["Range", "RoundRobin", "Sticky"]
    static readonly partitionAssiagnorBgClasses: string[] = ["bg-success", "bg-gradient-warning", "bg-danger"]
    static readonly partitionAssiagnorTextClasses: string[] = ["text-success", "text-warning", "text-danger"]
    static readonly partitionAssiagnorIcons: string[] = ["fab fa-buffer", "fas fa-random", "far fa-save"]

    static readonly typeBgClasses: string[] = ["bg-success", "bg-gradient-warning"]
    static readonly typeTextClasses: string[] = ["text-success", "text-warning"]
    static readonly typeIcons: string[] = ["far fa-smile-beam", "far fa-angry"]

    ///////////////////////////////////////////
    // STATIS methods
    ///////////////////////////////////////////

    static getStatusIndex(name: string): number  {
        const index = ConsumerGroupInfo.statusNames.findIndex(s => s.toUpperCase() === (name ? name.toUpperCase() : ''))
        return (index == -1) ? (ConsumerGroupInfo.statusNames.length - 1) : index
    }

    static getStatusIcon(status: string): string  {
        return ConsumerGroupInfo.statusIcons[this.getStatusIndex(status)]
    }

    static getStatusBgClass(status: string): string {
        return ConsumerGroupInfo.statusBgClasses[this.getStatusIndex(status)]
    }

    static getStatusTextClass(status: string): string {
        return ConsumerGroupInfo.statusTextClasses[this.getStatusIndex(status)]
    }

    ///////////////////////////////////////////
    // PARTITION ASSIGNOR methods
    ///////////////////////////////////////////

    static getPartitionAssignorIndex(name: string): number  {
        const index = ConsumerGroupInfo.partitionAssiagnorNames.findIndex(s => s.toUpperCase() === (name ? name.toUpperCase() : ''))
        return (index == -1) ? (ConsumerGroupInfo.partitionAssiagnorNames.length - 1) : index
    }

    static getPartitionAssignorIcon(status: string): string  {
        return ConsumerGroupInfo.partitionAssiagnorIcons[this.getPartitionAssignorIndex(status)]
    }

    static getPartitionAssignorBgClass(status: string): string {
        return ConsumerGroupInfo.partitionAssiagnorBgClasses[this.getPartitionAssignorIndex(status)]
    }

    static getPartitionAssignorTextClass(status: string): string {
        return ConsumerGroupInfo.partitionAssiagnorTextClasses[this.getPartitionAssignorIndex(status)]
    }

    ///////////////////////////////////////////
    // TYPE methods
    ///////////////////////////////////////////

    static getTypeIndex(data: boolean): number  {
        return data ? 0 : 1
    }

    static getTypeIcon(data: boolean): string  {
        return ConsumerGroupInfo.typeIcons[this.getTypeIndex(data)]
    }

    static getTypeBgClass(data: boolean): string {
        return ConsumerGroupInfo.typeBgClasses[this.getTypeIndex(data)]
    }

    static getTypeTextClass(data: boolean): string {
        return ConsumerGroupInfo.typeTextClasses[this.getTypeIndex(data)]
    }
}
