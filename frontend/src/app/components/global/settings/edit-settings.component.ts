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
      const formControl = new FormControl(currentSetting && currentSetting.value ? currentSetting.value : '');
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
          updatedData.push(new Setting(setting, formValue));
        }
      }
    });

    // Http call
    this.update.emit(updatedData);
    //this.settingForm.enable();
  }

}
