export class Plugin {
  class: string;
  type: string;
  version: string;

  constructor(pluginClass: string, type: string, version: string) {
      this.class = pluginClass;
      this.type = type
      this.version = version
  }
}
