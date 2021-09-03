import { ConnectorTask } from './connector-task.model';

export class Connector {
  name: string;
  type: string;
  connector: Object;
  config: Object;
  tasks: ConnectorTask[];
  status: string;

  constructor(name: string, type: string, connector: Object, config: Object, tasks: ConnectorTask[], status: string) {
      this.name = name
      this.type = type
      this.connector = connector
      this.config = config
      this.tasks = tasks
      this.status = status
  }

}
