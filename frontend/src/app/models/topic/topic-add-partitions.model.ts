export class TopicAddPartitions {
    nbPartitions: number;
    newAssignments: any;

    constructor(nbPartitions: number, newAssignments?: any) {
        this.nbPartitions = nbPartitions;
        this.newAssignments = newAssignments;
    }
}
