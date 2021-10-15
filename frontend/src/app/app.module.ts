import { NgModule, APP_INITIALIZER } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { NgxSpinnerModule } from "ngx-spinner";
import { NgSelectModule } from '@ng-select/ng-select';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { ToastrModule } from 'ngx-toastr';
import { AppComponent } from './app.component';
import { AuthenticatedComponent } from '@components/auth/authenticated.component';
import { ClusterComponent } from '@components/clusters/cluster-dashboard/cluster.component';
import { ClusterDetailComponent } from '@components/clusters/cluster-detail/cluster-detail.component';
import { ClustersDropdownMenuComponent } from '@components/header/clusters-dropdown-menu/clusters-dropdown-menu.component';
import { ClustersComponent } from '@components/clusters/list/clusters.component';
import { FooterComponent } from '@components/footer/footer.component';
import { HeaderComponent } from '@components/header/header.component';
import { MenuSidebarComponent } from '@components/menu-sidebar/menu-sidebar.component';
import { DatePipe } from '@angular/common';
import { UserDropdownMenuComponent } from '@components/header/user-dropdown-menu/user-dropdown-menu.component';
import { SettingsComponent } from '@components/settings/settings.component';
import { CardActionsDirective } from '@directives/card-actions.directive';
import { BrokersComponent } from '@components/brokers/brokers.component';
import { TopicsComponent } from '@components/topics/topics.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TopicsEditSettingsComponent } from './components/topics/topic-edit-settings/topic-edit-settings.component';
import { TopicCreateComponent } from './components/topics/topic-create/topic-create.component';
import { TopicDetailComponent } from './components/topics/topic-detail/topic-detail.component';
import { TopicAddPartitionsComponent } from './components/topics/topic-add-partitions/topic-add-partitions.component';
import { TopicReassignPartitionsComponent } from './components/topics/topic-reassign-partitions/topic-reassign-partitions.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { MultiTranslateHttpLoader } from './translate/MultipleTranslateHttpLoader';
import { ConsumerGroupsComponent } from '@components/consumer-groups/consumer-groups.component';
import { ConsumerGroupDetailComponent } from '@components/consumer-groups/consumer-group-detail/consumer-group-detail.component';
import { ConsumerGroupResetComponent } from '@components/consumer-groups/consumer-group-reset/consumer-group-reset.component';
import { ConsumerListComponent } from '@components/consumer-groups/consumer-list/consumer-list.component';
import { ConsumerGroupListComponent } from '@components/consumer-groups/consumer-group-list/consumer-group-list.component';
import { GaugeComponent } from '@components/global/gauge/gauge.component';
import { ChartLineComponent } from '@components/global/charts/line/chart-line.component';
import { TimeLineComponent } from '@components/global/charts/timeline/time-line.component';
import { ChartHeatComponent } from '@components/global/charts/heat/chart-heat.component';
import { SankeyComponent } from '@components/global/charts/sankey/sankey.component';
import { CheckComponent } from '@components/global/check/check.component';
import { ModalComponent } from '@components/global/modal/modal.component';
import { JsonViewerComponent } from '@components/global/json-viewer/json-viewer.component';
import { JsonViewerNodeComponent } from '@components/global/json-viewer/json-viewer-node/json-viewer-node.component';
import { BreadcrumbComponent } from '@components/global/breadcrumb/breadcrumb.component';
import { ContentHeaderComponent } from '@components/global/content-header/content-header.component';
import { TopicMetricsComponent } from '@components/metrics/topic/topic-metrics.component';
import { MessagesComponent } from '@components/messages/messages.component';
import { MessageEditComponent } from '@components/messages/message-edit/message-edit.component';
import { TopicsMetricsComponent } from '@components/metrics/topics/topics-metrics.component';
import { GlobalSettingsService } from '@services/global-settings.service';
import { CardIndicatorComponent } from '@components/global/card-indicator/card-indicator.component';
import { LoginComponent } from './components/login/login.component';
import { JwtModule } from '@auth0/angular-jwt';
import { EditSettingsComponent } from '@components/global/settings/edit-settings.component';
import { BrokerEditSettingsComponent } from '@components/brokers/broker-edit-settings/broker-edit-settings.component';
import { HasRoleDirective } from '@directives/has-role.directive';
import { LanguageDropdownMenuComponent } from './components/header/language-dropdown-menu/language-dropdown-menu.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ConnectDashboardComponent } from '@components/connect/connect-dashboard/dashboard.component';
import { ConnectCreateConnectorComponent } from '@components/connect/connector-create/create.component';
import { ConnectDetailConnectorComponent } from '@components/connect/connector-detail/detail.component';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

export function configurationInit(config: GlobalSettingsService) {
    return () => config.init();
}

@NgModule({
    declarations: [
        AppComponent,
        AuthenticatedComponent,
        ClusterDetailComponent,
        ClusterComponent,
        ClustersComponent,
        MenuSidebarComponent,
        HeaderComponent,
        FooterComponent,
        ClustersDropdownMenuComponent,
        LanguageDropdownMenuComponent,
        UserDropdownMenuComponent,
        SettingsComponent,
        CardActionsDirective,
        EditSettingsComponent,
        HasRoleDirective,
        BrokersComponent,
        BrokerEditSettingsComponent,
        TopicsComponent,
        TopicsEditSettingsComponent,
        TopicCreateComponent,
        TopicDetailComponent,
        TopicMetricsComponent,
        TopicsMetricsComponent,
        TopicAddPartitionsComponent,
        ConsumerGroupsComponent,
        ConsumerGroupDetailComponent,
        ConsumerListComponent,
        ConsumerGroupListComponent,
        GaugeComponent,
        ChartLineComponent,
        TimeLineComponent,
        SankeyComponent,
        ChartHeatComponent,
        CheckComponent,
        ModalComponent,
        JsonViewerComponent,
        JsonViewerNodeComponent,
        TopicReassignPartitionsComponent,
        ContentHeaderComponent,
        BreadcrumbComponent,
        ConsumerGroupResetComponent,
        MessagesComponent,
        MessageEditComponent,
        CardIndicatorComponent,
        LoginComponent,
        ConnectDashboardComponent,
        ConnectCreateConnectorComponent,
        ConnectDetailConnectorComponent
    ],
    imports: [
        AppRoutingModule,
        BrowserModule,
        BrowserAnimationsModule,
        NgxSpinnerModule,
        FormsModule,
        NgSelectModule,
        HttpClientModule,
        ReactiveFormsModule,
        ToastrModule.forRoot({
            timeOut: 3000,
            positionClass: 'toast-bottom-right',
            preventDuplicates: true,
        }),
        TranslateModule.forRoot({
            defaultLanguage: 'fr',
            loader: {
                provide: TranslateLoader,
                useClass: MultiTranslateHttpLoader,
                deps: [HttpClient]
            }
        }),
        JwtModule.forRoot({
            config: {
                tokenGetter: () => localStorage.getItem("token"),
                skipWhenExpired: true
            },
        }),
        NgxPaginationModule
    ],
    providers: [
        DatePipe,
        {
            provide: APP_INITIALIZER,
            useFactory: configurationInit,
            deps: [GlobalSettingsService],
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
