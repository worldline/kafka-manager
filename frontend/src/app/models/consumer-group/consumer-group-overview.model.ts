export class ConsumerGroupOverview {

  groupId: string;
  state: string;
  topicsLag: Map<string, number>;
  topic: string;
  lag: number;

  constructor(groupId: string, state: string, topicsLag: Map<string, number>) {
    this.groupId = groupId;
    this.state = state;
    this.topicsLag = topicsLag || new Map<string, number>();
  }

}
