export class ConnectorTask {
  id: string;
  state: string;
  workerId: string;
  connector: string;
  task: number;
  trace: string;

  constructor(id: string, state: string, workerId: string, connector: string, task: number, trace: string) {
      this.id = id
      this.state = state
      this.workerId = workerId
      this.connector = connector
      this.task = task
      this.trace = trace
  }
}
