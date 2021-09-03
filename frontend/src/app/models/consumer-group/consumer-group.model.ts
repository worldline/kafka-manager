import { ConsumerCoordinator } from './consumer-coordinator.model';
import { ConsumerMember } from './consumer-member.model';

export class ConsumerGroup {

  groupId: string;
  isSimpleConsumerGroup: boolean;
  members: ConsumerMember[];
  partitionAssignor: string;
  state: string;
  coordinator: ConsumerCoordinator;
  authorizedOperations: string[];
  topic: string;

  constructor(groupId: string, isSimpleConsumerGroup: boolean, members: ConsumerMember[], partitionAssignor: string, state: string, coordinator: ConsumerCoordinator,
              authorizedOperations: string[], topic: string) {
    this.groupId = groupId;
    this.isSimpleConsumerGroup = isSimpleConsumerGroup;
    this.members = members;
    this.partitionAssignor = partitionAssignor;
    this.state = state;
    this.coordinator = coordinator;
    this.authorizedOperations = authorizedOperations;
    this.topic = topic;
  }

}
