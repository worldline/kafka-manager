import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BrokersComponent } from '@components/brokers/brokers.component';
import { BrokerEditSettingsComponent } from '@components/brokers/broker-edit-settings/broker-edit-settings.component';
import { ClusterDetailComponent } from '@components/clusters/cluster-detail/cluster-detail.component';
import { ClustersComponent } from '@components/clusters/list/clusters.component';
import { SettingsComponent } from '@components/settings/settings.component';
import { TopicsComponent } from '@components/topics/topics.component';
import { ClusterResolver } from '@resolvers/cluster.resolver';
import { TopicResolver } from '@resolvers/topic.resolver';
import { BrokerResolver } from '@resolvers/broker.resolver';
import { BrokerListResolver } from '@resolvers/broker-list.resolver';
import { TopicsEditSettingsComponent } from '@components/topics/topic-edit-settings/topic-edit-settings.component';
import { TopicCreateComponent } from '@components/topics/topic-create/topic-create.component';
import { TopicDetailComponent } from '@components/topics/topic-detail/topic-detail.component';
import { TopicAddPartitionsComponent } from '@components/topics/topic-add-partitions/topic-add-partitions.component';
import { TopicReassignPartitionsComponent } from '@components/topics/topic-reassign-partitions/topic-reassign-partitions.component';
import { ConsumerGroupsComponent } from '@components/consumer-groups/consumer-groups.component';
import { ConsumerGroupDetailComponent } from '@components/consumer-groups/consumer-group-detail/consumer-group-detail.component';
import { ConsumerGroupResolver } from '@resolvers/consumer-group.resolver';
import { ConsumerGroupResetComponent } from '@components/consumer-groups/consumer-group-reset/consumer-group-reset.component';
import { TopicMetricsComponent } from '@components/metrics/topic/topic-metrics.component';
import { MessagesComponent } from '@components/messages/messages.component';
import { TopicsMetricsComponent } from '@components/metrics/topics/topics-metrics.component';
import { ClusterComponent } from '@components/clusters/cluster-dashboard/cluster.component';
import { ConnectDashboardComponent } from '@components/connect/connect-dashboard/dashboard.component';
import { ConnectCreateConnectorComponent } from '@components/connect/connector-create/create.component';
import { ConnectDetailConnectorComponent } from '@components/connect/connector-detail/detail.component';
import { ConnectPluginsResolver } from '@resolvers/connect-plugins.resolver';
import { ConnectorResolver } from '@resolvers/connector.resolver';
import { LoginComponent } from '@components/login/login.component';
import { AuthenticatedComponent } from '@components/auth/authenticated.component';
import { AuthGuard } from '@app/guard/auth.guard';

const routes: Routes = [
    {
        path: 'login',
        pathMatch: 'full',
        component: LoginComponent
    },
    {
        path: '',
        component: AuthenticatedComponent,
        canActivate: [ AuthGuard ],
        children: [
            {
                path: '',
                pathMatch: 'full',
                component: ClustersComponent,
                data: {
                    title: 'clusters.list.title'
                },
                canActivateChild: [ AuthGuard ]
            },
            {
                path: 'settings',
                component: SettingsComponent,
                data: {
                    breadcrumb: 'breadcrumb.settings',
                    title: 'settings.title'
                },
                canActivateChild: [ AuthGuard ]
            },
            {
                path: 'add-cluster',
                component: ClusterDetailComponent,
                data: {
                    breadcrumb: 'breadcrumb.cluster.add',
                    title: 'clusters.edit.new'
                },
                canActivateChild: [ AuthGuard ]
            },
            {
                path: 'clusters',
                canActivateChild: [ AuthGuard ],
                children: [
                    {
                        path: ':clusterId',
                        resolve: {
                            cluster: ClusterResolver
                        },
                        canActivateChild: [AuthGuard],
                        children: [
                            {
                                path: '',
                                pathMatch: 'full',
                                component: ClusterComponent,
                                data: {
                                    title: 'clusters.home.title',
                                    breadcrumb: 'breadcrumb.cluster.home'
                                },
                                canActivateChild: [AuthGuard]
                            },
                            {
                                path: 'details',
                                component: ClusterDetailComponent,
                                data: {
                                    title: 'clusters.edit.title',
                                    breadcrumb: 'breadcrumb.cluster.detail'
                                },
                                canActivateChild: [AuthGuard]
                            },
                            {
                                path: 'monitoring',
                                component: TopicsMetricsComponent,
                                data: {
                                    breadcrumb: 'breadcrumb.monitoring',
                                    title: 'metrics.topics.title'
                                },
                                canActivateChild: [AuthGuard]
                            },
                            {
                                path: 'add-topic',
                                component: TopicCreateComponent,
                                data: {
                                    breadcrumb: 'breadcrumb.topics.add',
                                    title: 'topics.list.title'
                                },
                                canActivateChild: [AuthGuard]
                            },
                            {
                                path: 'brokers',
                                data: {
                                    breadcrumb: 'breadcrumb.brokers.list',
                                    title: 'brokers.title'
                                },
                                children: [
                                    {
                                        path: '',
                                        pathMatch: 'full',
                                        component: BrokersComponent
                                    },
                                    {
                                        path: ':brokerId',
                                        component: BrokerEditSettingsComponent,
                                        data: {
                                            title: 'settings-edit.title',
                                            breadcrumb: 'breadcrumb.brokers.settings',
                                        },
                                        resolve: {
                                            broker: BrokerResolver
                                        }
                                    }
                                ],
                                canActivateChild: [AuthGuard]
                            },
                            {
                                path: 'consumer-groups',
                                canActivateChild: [AuthGuard],
                                children: [
                                    {
                                        path: '',
                                        pathMatch: 'full',
                                        component: ConsumerGroupsComponent,
                                        canActivateChild: [AuthGuard],
                                        data: {
                                            title: 'consumerGroups.list.title',
                                            breadcrumb: 'breadcrumb.consumerGroups.list',
                                        }
                                    },
                                    {
                                        path: ':consumerGroupId',
                                        resolve: {
                                            consumerGroup: ConsumerGroupResolver
                                        },
                                        runGuardsAndResolvers: 'always',
                                        canActivateChild: [AuthGuard],
                                        children: [
                                            {
                                                path: '',
                                                pathMatch: 'full',
                                                component: ConsumerGroupDetailComponent,
                                                data: {
                                                    title: 'consumerGroups.detail.title',
                                                    breadcrumb: 'breadcrumb.consumerGroups.detail',
                                                }
                                            },
                                            {
                                                path: 'reset-offsets',
                                                component: ConsumerGroupResetComponent,
                                                data: {
                                                    title: 'consumerGroups.reset.title',
                                                    breadcrumb: 'breadcrumb.consumerGroups.reset',
                                                },
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                path: 'messages',
                                component: MessagesComponent,
                                pathMatch: 'full',
                                data: {
                                    title: 'messages.title',
                                    breadcrumb: 'breadcrumb.messages.read'
                                },
                            },
                            {
                                path: 'connect',
                                children: [
                                    {
                                        path: '',
                                        pathMatch: 'full',
                                        component: ConnectDashboardComponent,
                                        data: {
                                            title: 'connect.dashboard.title',
                                            breadcrumb: 'breadcrumb.connect.dashboard'
                                        }
                                    },
                                    {
                                        path: 'add-connector',
                                        component: ConnectCreateConnectorComponent,
                                        runGuardsAndResolvers: 'always',
                                        resolve: {
                                            plugins: ConnectPluginsResolver
                                        },
                                        data: {
                                            title: 'connect.add-connector.title',
                                            breadcrumb: 'breadcrumb.connect.add-connector'
                                        }
                                    },
                                    {
                                        path: ':connectorName',
                                        runGuardsAndResolvers: 'always',
                                        resolve: {
                                            connector: ConnectorResolver
                                        },
                                        children: [
                                            {
                                                path: '',
                                                component: ConnectDetailConnectorComponent,
                                                runGuardsAndResolvers: 'always',
                                                data: {
                                                    title: 'connect.connector-detail.title',
                                                    breadcrumb: 'breadcrumb.connect-detail.connector'
                                                }
                                            },
                                            {
                                                path: 'edit',
                                                runGuardsAndResolvers: 'always',
                                                resolve: {
                                                    plugins: ConnectPluginsResolver
                                                },
                                                data: {
                                                    title: 'connect.edit-connector.title',
                                                    breadcrumb: 'breadcrumb.connect.edit-connector'
                                                },
                                                component: ConnectCreateConnectorComponent
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                path: 'topics',
                                data: {
                                    breadcrumb: 'breadcrumb.topics.list'
                                },
                                children: [
                                    {
                                        path: '',
                                        pathMatch: 'full',
                                        component: TopicsComponent,
                                        data: {
                                            title: 'topics.list.title'
                                        }
                                    },
                                    {
                                        path: ':topicName',
                                        runGuardsAndResolvers: 'always',
                                        resolve: {
                                            topic: TopicResolver
                                        },                                        
                                        children: [
                                            {
                                                path: '',
                                                component: TopicDetailComponent,
                                                data: {
                                                    title: 'topic-detail.title',
                                                    breadcrumb: 'breadcrumb.topics.detail'
                                                }
                                            },
                                            {
                                                path: 'metrics',
                                                component: TopicMetricsComponent,
                                                data: {
                                                    title: 'metrics.topic.title',
                                                    breadcrumb: 'breadcrumb.topics.metrics'
                                                }
                                            },
                                            {
                                                path: 'add-partitions',
                                                component: TopicAddPartitionsComponent,
                                                data: {
                                                    title: 'topics.addPartitions.title',
                                                    breadcrumb: 'breadcrumb.topics.partitions.add'
                                                }
                                            },
                                            {
                                                path: 'reassign-partitions',
                                                component: TopicReassignPartitionsComponent,
                                                runGuardsAndResolvers: 'always',
                                                resolve: {
                                                    brokers: BrokerListResolver
                                                },
                                                data: {
                                                    title: 'topics.reassignPartitions.title',
                                                    breadcrumb: 'breadcrumb.topics.partitions.reassign'
                                                }
                                            },
                                            {
                                                path: 'settings/edit',
                                                component: TopicsEditSettingsComponent,
                                                data: {
                                                    title: 'topics.settings-edit.title',
                                                    breadcrumb: 'breadcrumb.topics.settings'
                                                }
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ],
    providers: [ AuthGuard ]
})
export class AppRoutingModule {}
