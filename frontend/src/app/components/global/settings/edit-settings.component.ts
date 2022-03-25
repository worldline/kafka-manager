import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Setting } from '@models/setting/setting.model';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-edit-settings',
  templateUrl: './edit-settings.component.html',
  styleUrls: ['./edit-settings.component.css']
})
export class EditSettingsComponent implements OnInit {

  constructor() { }

  @Input() resourceSettings: Setting[];
  @Input() settings: any;
  @Input() intlPrefix: string;
  settingList: string[];
  @Input() resourceId: string;
  @Output() update = new EventEmitter<any>();
  settingForm: FormGroup = new FormGroup({});

  ngOnInit(): void {
    this.settingList = Object.keys(this.settings).sort();
    this.settingList.forEach(setting => {
      const currentSetting = this.resourceSettings.find(a => a.key == setting);
      let values: any = currentSetting && currentSetting.value ? currentSetting.value : '';
      let formControl;

      // Split if multiple values
      if (this.settings[setting].multiple) {
        if (values.includes(',')) {
          values = values.split(',');
        } else {
          values = [values];
        }
      }

      formControl = new FormControl(values);

      if (this.settings[setting] && this.settings[setting].readOnly) {
        formControl.disable();
      }
      this.settingForm.addControl(setting, formControl);
    });
  }

  intlKey(key) {
    return key.replaceAll('.', '-');
  }

  isNotDefault(key) {
    const entryValue = this.settingForm.controls[key] ? this.settingForm.controls[key].value : '';
    if (Array.isArray(entryValue)) {
      return entryValue && (entryValue.length > 1 || !entryValue.includes(this.settings[key].default));
    }
    return entryValue && this.settings[key].default !== entryValue;
  }

  getInputType(key) {
    const setting = this.settings[key];
    if (setting && setting.type === 'number') {
      return 'number';
    }
    return 'text';
  }

  getFormType(key) {
    const setting = this.settings[key];
    if (setting) {
      if (setting.type === 'boolean') {
        return 'boolean';
      } else if (setting.values) {
        return 'values'
      }
    }
    return 'default';
  }

  onSubmit() {
    this.settingForm.disable();

    // Get data to update
    const updatedData: Setting[] = [];
    this.settingList.forEach(setting => {
      const formValue = this.settingForm.controls[setting].value;
      if (formValue) {
        const currentSetting = this.resourceSettings.find(a => a.key == setting);
        if (!currentSetting || currentSetting.value !== formValue) {
          // Manage multiple values
          let valueToAdd;
          if (Array.isArray(formValue)) {
            valueToAdd = formValue.join(',');
          } else {
            valueToAdd = formValue;
          }


          // Add value
          updatedData.push(new Setting(setting, valueToAdd));
        }
      }
    });

    // Http call
    this.update.emit(updatedData);
    //this.settingForm.enable();
  }

}
