export class ValidatePluginValue {
  name: string;
  value: string;
  errors: string[];
  visible: boolean;
}

export class ValidatePluginDefinition {
  name: string;
  type: string;
  required: boolean;
  default_value: string;
  importance: string;
  documentation: string;
  group: string;
  width: string;
  displayName: string;
  order: number;
}

export class ValidatePluginConfiguration {
  definition: ValidatePluginDefinition;
  value: ValidatePluginValue;

  constructor(definition: ValidatePluginDefinition, value: ValidatePluginValue) {
    this.definition = definition
    this.value = value
  }
}

export class ValidatePluginResponse {
  name: string;
  errorCount: number;
  groups: string[];
  configs: ValidatePluginConfiguration[];

  constructor(name: string, errorCount: number, groups: string[], configs: ValidatePluginConfiguration[]) {
      this.name = name
      this.errorCount = errorCount
      this.groups = groups
      this.configs = configs
  }
}
