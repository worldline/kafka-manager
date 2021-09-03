import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy, Renderer2 } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '@services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
    public loginForm: FormGroup;
    public isAuthLoading = false;
    constructor(
        private renderer: Renderer2,
        private router: Router,
        private toastrService: ToastrService,
        private authService: AuthService,
        private translateService: TranslateService
    ) { }

    ngOnInit() {

        if(this.authService.isAuthenticated()) {
            this.router.navigate(["/"])
        }

        this.renderer.addClass(document.querySelector('app-root'), 'login-page');
        this.loginForm = new FormGroup({
            username: new FormControl(null, Validators.required),
            password: new FormControl(null, Validators.required),
        });
    }

    login() {
        if (this.loginForm.valid) {
            this.loginForm.disable();
            const username = this.loginForm.controls['username'].value as string;
            const password = this.loginForm.controls['password'].value as string;

            this.authService.login(username, password)
                .subscribe(() => {
                    this.router.navigateByUrl(this.authService.redirectUrl)
                },
                (error: HttpErrorResponse) => {
                    this.loginForm.enable();
                    if(error.status === 403) {
                        this.toastrService.error(this.translateService.instant("login.modal.wrong-identifier"), this.translateService.instant("login.modal.title"))
                    } else {
                        this.toastrService.error(this.translateService.instant("login.modal.error", { error : error.status}), this.translateService.instant("login.modal.title"))
                    }
                });
        } else {
            this.toastrService.error(this.translateService.instant("login.modal.incorrect"), this.translateService.instant("login.modal.title"));
        }
    }

    ngOnDestroy() {
        this.renderer.removeClass(document.querySelector('app-root'), 'login-page');
    }
}
