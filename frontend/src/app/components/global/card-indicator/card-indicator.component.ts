import { Component, Input } from '@angular/core';
import { CardIndicator } from '@models/card-indicator/card-indicator.model';

@Component({
  selector: 'app-card-indicator',
  templateUrl: './card-indicator.component.html',
  styleUrls: ['./card-indicator.component.scss'],
})
export class CardIndicatorComponent {

    @Input() cardIndicator: CardIndicator

}
