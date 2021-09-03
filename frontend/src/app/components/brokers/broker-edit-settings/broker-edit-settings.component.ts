import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Setting } from '@models/setting/setting.model';
import { environment } from 'environments/environment';
import { FormGroup, FormControl } from '@angular/forms';
import { BrokerService } from '@services/broker.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-broker-edit-settings',
  templateUrl: './broker-edit-settings.component.html'
})
export class BrokerEditSettingsComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private brokerService: BrokerService,
              private toastr: ToastrService,
              private router: Router,
              private translate: TranslateService) { }

  brokerSettings: Setting[];
  settings: any = environment.brokerSettingList;
  brokerId: string;

  ngOnInit(): void {
    this.brokerSettings = this.route.snapshot.data.broker.configuration || [];
    this.brokerId = this.route.snapshot.paramMap.get('brokerId');
  }

  onSubmit(updatedData) {
    const clusterId = this.route.snapshot.data.cluster.id;

    // Http call
    this.brokerService.updateSettings(clusterId, this.brokerId, updatedData).subscribe(
      () => {
        this.toastr.success(this.translate.instant("brokerSettings.messages.success.text", {brokerId: this.brokerId}), this.translate.instant("brokerSettings.messages.success.title"));
        this.router.navigateByUrl(`/clusters/${clusterId}/topics/${this.brokerId}`);
      }, () => {
        this.toastr.error(this.translate.instant("brokerSettings.messages.error.text", {brokerId: this.brokerId}), this.translate.instant("brokerSettings.messages.error.title"));
      }
    );
  }

}
