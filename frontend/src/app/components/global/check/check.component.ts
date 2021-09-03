import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-check',
  templateUrl: './check.component.html',
})
export class CheckComponent {

    @Input() check: boolean;

}
