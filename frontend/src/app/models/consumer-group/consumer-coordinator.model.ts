export class ConsumerCoordinator {

  id: string;
	host: string;
	port: number;
	rack: string;

  constructor(id: string, host: string, port: number, rack: string) {
    this.id = id;
    this.host = host;
    this.port = port;
    this.rack = rack;
  }

}