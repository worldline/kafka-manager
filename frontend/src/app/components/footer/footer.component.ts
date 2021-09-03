import { Component, OnInit } from '@angular/core';
import { version } from './../../../../package.json';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent implements OnInit {
  public appVersion = version;
  constructor() {}

  ngOnInit() {}
}
