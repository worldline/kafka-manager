export class CreateConnector {
  name: string;
  config: Object;

  constructor(name: string, config: Object) {
      this.name = name
      this.config = config
  }

}
