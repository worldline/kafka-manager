import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { JsonViewerNodeComponent } from './json-viewer-node/json-viewer-node.component';
import * as moment from 'moment';

@Component({
  selector: 'app-json-viewer',
  templateUrl: './json-viewer.component.html',
  styleUrls: ['./json-viewer.component.scss'],
})
export class JsonViewerComponent {

  @Input() data: any;
  @Input() dateFormat: string = "DD/MM/YYYY HH:mm:SS";
  @Output() select = new EventEmitter<any>();
  @Output() edit = new EventEmitter<any>();

  @ViewChild(JsonViewerNodeComponent) child;

}
