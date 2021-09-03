import { Component, OnInit, DoCheck, KeyValueChanges, KeyValueDiffer, KeyValueDiffers, Input, Output, ViewChildren, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import * as uuid from 'uuid';

@Component({
  selector: 'app-json-viewer-node',
  templateUrl: './json-viewer-node.component.html',
  styleUrls: ['./json-viewer-node.component.scss'],
})
export class JsonViewerNodeComponent implements OnInit, DoCheck {

  @Input() data: any;
  @Input() dateFormat: string = "DD/MM/YYYY HH:mm:SS";
  @Input() displayMenuNode: boolean = false;
  @Input() sortKey: string;
  @Output() select = new EventEmitter<any>();
  @Output() edit = new EventEmitter<any>();
  @Output() open = new EventEmitter<boolean>();

  @ViewChildren(JsonViewerNodeComponent) children;
  currentId: string = uuid.v4();
  private openStatus: any = {};
  private dataDiffer: KeyValueDiffer<string, any>;
  updatedKey: string[] = [];
  isSelected: any = {};

  constructor(private toastr: ToastrService,
              private translate: TranslateService,
              private differs: KeyValueDiffers) {}

  ngOnInit() {
    // Init deffer watcher
    this.dataDiffer = this.differs.find(this.data).create();

    // Init status
    Object.keys(this.data).forEach(key => {
      this.openStatus[key] = false;
      this.isSelected[key] = false;
    });
  }

  ngDoCheck(): void {
      const changes = this.dataDiffer.diff(this.data);
      if (changes) {
        changes.forEachChangedItem((record) => {
          // Check if the json object are the same. Sometime, changes are detected even if nothing changed
          const isSameValues = JSON.stringify(record.currentValue) === JSON.stringify(record.previousValue);
          if (!isSameValues) {
            // Open updated node
            if (this.isObj(record.currentValue)) {
              this.openAction(record.key);
            }
            // Mark node as updated
            if (!this.updatedKey.includes(record.key)) {
              this.updatedKey.push(record.key);
            }
          }
        });
      }
  }

  sortData = (a, b) => {
    if (this.sortKey) {
      return a[this.sortKey] - b[this.sortKey];
    }
  }

  identify(index: number, data: any) {
    return data.key;
  }

  isUpdated(key) {
    return this.updatedKey.includes(key);
  }

  toggleOpen(index) {
    this.openStatus[index] = !this.openStatus[index];
  }

  closeAll() {
    Object.keys(this.openStatus).forEach(key => this.openStatus[key] = false);
    if (this.children && this.children.length > 0) {
      this.children.forEach(child => child.closeAll());
    }
  }

  openAll() {
    Object.keys(this.openStatus).forEach(key => this.openStatus[key] = true);
    if (this.children && this.children.length > 0) {
      this.children.forEach(child => child.openAll());
    }
  }

  openAction(index, value = true) {
    // Select for update
    if (this.displayMenuNode) {
      this.selectAction(index, this.data[index], true);
    }
    if (this.openStatus[index] !== value) {
      this.openStatus[index] = value;
    }
    // Wait refresh before send the action to the parent node
    setTimeout(() => {
      this.open.emit(value);
    }, 50)
  }

  isObj(data) {
    return typeof data === 'object';
  }

  nbSubNodes(data) {
    return Object.keys(data).length;
  }

  hasTransformValue(key, data) {
    return !!this.transformValue(key, data);
  }

  transformValue(key, data) {
    // Manage dates
    const keyDates = ['timestamp', 'birthdate', 'birthday', 'date'];
    if (!isNaN(data)) {
      const date = new Date(data);
      if (date.getTime() > 0 && !isNaN(date.getTime()) && isFinite(date.getTime()) && (keyDates.includes(key) || key.includes('Date'))) {
        return moment(date).format(this.dateFormat);
      }
    }
  }

  selectAction(key, value, isSelected) {
    this.isSelected[key] = isSelected;
    this.select.emit({
      isChecked: isSelected,
      node: value
    });
  }

  editAction(key, value) {
    this.edit.emit(value);
  }

  toClipboard(node) {
    navigator.clipboard.writeText(JSON.stringify(node)).then(() => {
      this.toastr.success(this.translate.instant("messages.jsonViewer.messages.clipboard.success.text"), this.translate.instant("messages.jsonViewer.messages.clipboard.success.title"));
    }, () => {
      this.toastr.error(this.translate.instant("messages.jsonViewer.messages.clipboard.error.text"), this.translate.instant("messages.jsonViewer.messages.clipboard.error.title"));
    });
  }

}
