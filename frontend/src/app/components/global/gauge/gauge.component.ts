import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-gauge',
  templateUrl: './gauge.component.html',
  styleUrls: ['./gauge.component.scss'],
})
export class GaugeComponent {

    @Input() rotation: number;
    @Input() color: string = "green";
    @Input() width: string = "100px";
    @Input() background: string = "#e9ecef";
    @Input() data: string;

}
