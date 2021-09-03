import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Setting } from '@models/setting/setting.model';
import { environment } from 'environments/environment';
import { FormGroup, FormControl } from '@angular/forms';
import { TopicService } from '@services/topic.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-topic-edit-settings',
  templateUrl: './topic-edit-settings.component.html'
})
export class TopicsEditSettingsComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private topicService: TopicService,
              private toastr: ToastrService,
              private router: Router,
              private translate: TranslateService) { }

  topicSettings: Setting[];
  settings: any = environment.topicSettingList;
  topicName: string;

  ngOnInit(): void {
    this.topicSettings = this.route.snapshot.data.topic.configuration || [];
    this.topicName = this.route.snapshot.paramMap.get('topicName');
  }

  onSubmit(updatedData) {
    const clusterId = this.route.snapshot.data.cluster.id;

    // Http call
    this.topicService.updateSettings(clusterId, this.topicName, updatedData).subscribe(
      () => {
        this.toastr.success(this.translate.instant("topics.settings-edit.messages.success.text", {topicName: this.topicName}), this.translate.instant("topics.settings-edit.messages.success.title"));
        this.router.navigateByUrl(`/clusters/${clusterId}/topics/${this.topicName}`);
      }, () => {
        this.toastr.error(this.translate.instant("topics.settings-edit.messages.error.text", {topicName: this.topicName}), this.translate.instant("topics.settings-edit.messages.error.title"));
      }
    );
  }

}
