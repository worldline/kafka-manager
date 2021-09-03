import { Component, OnInit } from '@angular/core';
import { ConnectService } from '@services/connect.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Plugin } from '@models/connect/plugin.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CreateConnector } from '@models/connect/create-connector.model';
import { UpdateConnector } from '@models/connect/update-connector.model';
import { Connector } from '@models/connect/connector.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import * as uuid from 'uuid';
import { ValidatePluginRequest } from '@models/connect/validate-plugin-request.model';
import { ValidatePluginResponse } from '@models/connect/validate-plugin-response.model';

@Component({
    selector: 'app-connect-create-connector',
    templateUrl: './create.component.html',
    styleUrls: ['./create.component.scss'],
})
export class ConnectCreateConnectorComponent implements OnInit {

    connectorForm = new FormGroup({
        name: new FormControl('', Validators.required),
        topic: new FormControl('', Validators.required),
        type: new FormControl('', Validators.required),
        plugin: new FormControl('', Validators.required)
    });
    plugins: Object;
    currentPlugins: Plugin[];
    configuration = [];
    pluginInformation: ValidatePluginResponse;
    connector: Connector;
    editMode: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private connectService: ConnectService,
        private toastr: ToastrService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        const plugins = this.route.snapshot.data.plugins;
        this.plugins = {};
        if (plugins) {
            plugins.forEach(p => {
                if (!this.plugins[p.type]) {
                    this.plugins[p.type] = [];
                }
                this.plugins[p.type].push(p);
            });
        }

        // Edit mode
        this.connector = this.route.snapshot.data.connector;
        this.editMode = !!this.connector;
        if (this.connector) {
            this.updateFormValueForEdition(this.connectorForm.controls.name, this.connector.name);
            this.updateFormValueForEdition(this.connectorForm.controls.plugin, this.connector.config['connector.class']);
            this.updateFormValueForEdition(this.connectorForm.controls.topic, this.connector.config['topic'] || this.connector.config['topics']);
            this.updateFormValueForEdition(this.connectorForm.controls.type, this.connector.type);
            this.selectPlugin(this.connector.config['connector.class']);
        }
    }

    updateFormValueForEdition(input, value, disable = true) {
        input.setValue(value);
        if (disable) {
            input.disable();
        }
    }

    addConfiguration(key = '', value = '', type: string = 'string', defaultValue = '', required: boolean = false, documentation: string = '') {
        const id = uuid.v4();
        this.connectorForm.addControl(`config-key-${id}`, new FormControl(key, Validators.required))
        this.connectorForm.addControl(`config-value-${id}`, new FormControl(value))
        if (required) {
            this.connectorForm.controls[`config-value-${id}`].setValidators(Validators.required);
        }
        if (defaultValue) {
            this.connectorForm.controls[`config-value-${id}`].setValue(defaultValue);
        }
        const data = { id: id, data: {
            key: key,
            value: value,
            type: type,
            documentation: documentation,
            required: required
        } };
        this.configuration.push(data);
    }

    removeConfiguration(id: string) {
        this.configuration = this.configuration.filter(item => item.id !== id);
        this.connectorForm.removeControl(`config-key-${id}`);
        this.connectorForm.removeControl(`config-value-${id}`);
    }

    onSubmit() {
        // Disable form
        this.connectorForm.disable();

        // Create submit request
        const connectorConfiguration = this.createConfiguration();
        let dataRequest = null;
        if (!this.editMode) {
            dataRequest = new CreateConnector(
                this.connectorForm.controls.name.value,
                connectorConfiguration
            );
        } else {
            dataRequest = new UpdateConnector(
                connectorConfiguration
            );  
        }
        
         // Validate
         const clusterId = this.route.snapshot.data.cluster.id;
         const request: ValidatePluginRequest = new ValidatePluginRequest(connectorConfiguration);
         this.connectService.validatePlugin(clusterId, request).subscribe(data => console.log(data));
 
         // Http call
         const connectorName = this.connector ? this.connector.name : dataRequest.name;
         let response = null;
         const type = this.editMode ? 'edit' : 'create';
         const redirectUrl = this.editMode ? `/clusters/${clusterId}/connect/${connectorName}` : `/clusters/${clusterId}/connect`;
         if (!this.editMode) {
            response = this.connectService.createConnector(clusterId, dataRequest);
         } else {
            response = this.connectService.updateConnector(clusterId, connectorName, dataRequest);
         }
         response.subscribe(
         () => {
             this.toastr.success(this.translate.instant(`connect.add-connector.messages.${type}.success.text`, {connectorName: connectorName}), this.translate.instant(`connect.add-connector.messages.${type}.success.title`));
             this.router.navigateByUrl(redirectUrl);
         }, () => {
             this.toastr.error(this.translate.instant(`connect.add-connector.messages.${type}.error.text`), this.translate.instant(`connect.add-connector.messages.${type}.error.title`));
             this.connectorForm.enable();
         }
         );
    }

    createConfiguration(): Object {
        const config = {};

        // Default values
        config['connector.class'] = this.connectorForm.controls.plugin.value
        config['name'] = this.connectorForm.controls.name.value
        if (this.connectorForm.controls.type.value === 'sink') {
            config['topics'] = this.connectorForm.controls.topic.value
        } else {
            config['topic'] = this.connectorForm.controls.topic.value
        }

        // Configuration values
        this.configuration.forEach(item => {
            const key = this.connectorForm.controls[`config-key-${item.id}`].value;
            const value = this.connectorForm.controls[`config-value-${item.id}`].value;
            if (value) {
                config[key] = value;
            }
        });
        return config;
    }

    getType(type: string) {
        switch(type) {
            case 'int':
            case 'long':
            case 'short':
                return 'number';
            case 'boolean':
                return 'boolean';
            default:
                return 'default';
        }
    }

    selectType(type: string) {
        this.currentPlugins = this.plugins[type];
        this.connectorForm.controls.plugin.setValue('');
    }

    selectPlugin(plugin: string) {
        const request: ValidatePluginRequest = new ValidatePluginRequest({
            'connector.class': plugin,
            'topics': 'test'
        });
        const clusterId = this.route.snapshot.data.cluster.id;
        this.connectService.validatePlugin(clusterId, request).subscribe(data => {
            // Manage required values
            this.configuration = [];
            data.configs.forEach(config => {
                if (!this.configuration.some(c => c.data.key === config.definition.name)
                    && config.definition.name !== 'connector.class'
                    && config.definition.name !== 'name'
                    && config.definition.name !== 'topic'
                    && config.definition.name !== 'topics') {
                        let defaultValue = config.definition.default_value;
                        if (this.connector && this.connector.config[config.definition.name]) {
                            defaultValue = this.connector.config[config.definition.name];
                        }
                        this.addConfiguration(
                            config.definition.name,
                            '',
                            config.definition.type.toLocaleLowerCase(),
                            defaultValue,
                            config.definition.required,
                            config.definition.documentation);
                }
            });
            // If connector exists
            if (this.connector) {
                Object.keys(this.connector.config)
                    .filter(config => config !== 'connector.class' && config !== 'name' && config !== 'topic'
                        && config !== 'topics' && !this.configuration.some(d => d.data.key === config))
                    .forEach(config => {
                        this.addConfiguration(config, this.connector.config[config]);
                    });
            }
            // Add default configuration if no one is set
            if (this.configuration.length === 0) {
                this.addConfiguration();
            }
            this.pluginInformation = data;
        })
    }

}
