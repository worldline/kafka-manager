import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import * as cloneDeep from 'lodash/cloneDeep';

@Component({
    selector: 'app-message-edit',
    templateUrl: './message-edit.component.html',
    styleUrls: ['./message-edit.component.scss']
})
export class MessageEditComponent implements OnInit {

    @Input() data: any;
    @Output() update = new EventEmitter<string>();
    @Output() cancel = new EventEmitter<void>();
    isJson: boolean = false;
    private initialData: any;

    form = new FormGroup({
        data: new FormControl('', Validators.required)
    });

    constructor(private toastr: ToastrService,
                private translate: TranslateService) {
    }

    ngOnInit(): void {
        // Manage data type
        let data = this.data;
        if (typeof data === 'object') {
            data = JSON.stringify(data, null, 2);
            this.isJson = true;
        }

        // Save initial value
        this.initialData = cloneDeep(data);

        // Update form data
        this.form.patchValue({
            data: data
        });
    }

    reset(): void {
        this.form.patchValue({
            data: this.initialData
        });
    }

    onSubmit() {
        let data = this.form.controls.data.value;
        if (this.isJson) {
            try {
                data = JSON.parse(data);
            } catch(e) {
                this.toastr.error(this.translate.instant("messages.editMessage.messages.json.error.text"), this.translate.instant("messages.editMessage.messages.json.error.title"));
                return;
            }
        }
        this.update.emit(data);
    }

}
