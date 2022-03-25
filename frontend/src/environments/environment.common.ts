export const commonEnvironment = {
    brokerSettingList: {
        'advertised.host.name': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'advertised.listeners': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'advertised.port': {
            'default': 'null',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'alter.config.policy.class.name': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'alter.log.dirs.replication.quota.window.num': {
            'default': '11',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'alter.log.dirs.replication.quota.window.size.seconds': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'authorizer.class.name': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'auto.create.topics.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'auto.leader.rebalance.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'background.threads': {
            'default': '10',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'broker.id': {
            'default': '-1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'broker.id.generation.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'broker.rack': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'client.quota.callback.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'compression.type': {
            'default': 'producer',
            'mode': 'cluster-wide'
        },
        'confluent.authorizer.authority.name': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.disk.max.load': {
            'default': '0.85',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'confluent.balancer.exclude.topic.names': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.exclude.topic.prefixes': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.heal.broker.failure.threshold.ms': {
            'default': '3600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.heal.uneven.load.trigger': {
            'default': 'EMPTY_BROKER',
            'mode': 'cluster-wide',
            'values': [
                'ANY_UNEVEN_LOAD',
                'EMPTY_BROKER'
            ]
        },
        'confluent.balancer.max.replicas': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.network.in.max.bytes.per.second': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.network.out.max.bytes.per.second': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.balancer.throttle.bytes.per.second': {
            'default': '10485760',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'confluent.cluster.link.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.cluster.link.io.max.bytes.per.second': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'confluent.cluster.link.replication.quota.window.num': {
            'default': '11',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.cluster.link.replication.quota.window.size.seconds': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.log.placement.constraints': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.metadata.server.cluster.registry.clusters': {
            'default': '[]',
            'mode': 'cluster-wide'
        },
        'confluent.offsets.topic.placement.constraints': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.reporters.telemetry.auto.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'confluent.security.event.logger.authentication.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.security.event.logger.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.security.event.router.config': {
            'default': '',
            'mode': 'cluster-wide'
        },
        'confluent.tier.archiver.num.threads': {
            'default': '2',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.backend': {
            'default': '',
            'mode': 'read-only',
            'values': [
                'S3',
                'GCS',
                'AzureBlockBlob',
                'mock',
                ''
            ],
            'readOnly': true
        },
        'confluent.tier.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'confluent.tier.feature': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.fenced.segment.delete.delay.ms': {
            'default': '600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.fetcher.num.threads': {
            'default': '4',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.gcs.bucket': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.gcs.cred.file.path': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.gcs.prefix': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.gcs.region': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.local.hotset.bytes': {
            'default': '-1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'confluent.tier.local.hotset.ms': {
            'default': '86400000',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'confluent.tier.max.partition.fetch.bytes.override': {
            'default': '0',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.metadata.bootstrap.servers': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.metadata.replication.factor': {
            'default': '3',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.s3.bucket': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.s3.cred.file.path': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.s3.prefix': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.s3.region': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.tier.topic.delete.check.interval.ms': {
            'default': '10800000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'confluent.transaction.state.log.placement.constraints': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'connection.failed.authentication.delay.ms': {
            'default': '100',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'connections.max.idle.ms': {
            'default': '600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'connections.max.reauth.ms': {
            'default': '0',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'control.plane.listener.name': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'controlled.shutdown.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'controlled.shutdown.max.retries': {
            'default': '3',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'controlled.shutdown.retry.backoff.ms': {
            'default': '5000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'controller.quota.window.num': {
            'default': '11',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'controller.quota.window.size.seconds': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'controller.socket.timeout.ms': {
            'default': '30000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'create.topic.policy.class.name': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'default.replication.factor': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'delegation.token.expiry.check.interval.ms': {
            'default': '3600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'delegation.token.expiry.time.ms': {
            'default': '86400000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'delegation.token.master.key': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'delegation.token.max.lifetime.ms': {
            'default': '604800000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'delete.records.purgatory.purge.interval.requests': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'delete.topic.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'enable.fips': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'fetch.max.bytes': {
            'default': '57671680',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'fetch.purgatory.purge.interval.requests': {
            'default': '1000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'follower.replication.throttled.rate': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'follower.replication.throttled.replicas': {
            'default': 'none',
            'mode': 'cluster-wide',
            'values': [
                'none',
                '*'
            ]
        },
        'group.initial.rebalance.delay.ms': {
            'default': '3000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'group.max.session.timeout.ms': {
            'default': '1800000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'group.max.size': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'group.min.session.timeout.ms': {
            'default': '6000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'host.name': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'inter.broker.listener.name': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'inter.broker.protocol.version': {
            'default': '2.7-IV2',
            'mode': 'read-only',
            'values': [
                '0.8.0',
                '0.8.1',
                '0.8.2',
                '0.9.0',
                '0.10.0-IV0',
                '0.10.0-IV1',
                '0.10.1-IV0',
                '0.10.1-IV1',
                '0.10.1-IV2',
                '0.10.2-IV0',
                '0.11.0-IV0',
                '0.11.0-IV1',
                '0.11.0-IV2',
                '1.0-IV0',
                '1.1-IV0',
                '2.0-IV0',
                '2.0-IV1',
                '2.1-IV0',
                '2.1-IV1',
                '2.1-IV2',
                '2.2-IV0',
                '2.2-IV1',
                '2.3-IV0',
                '2.3-IV1',
                '2.4-IV0',
                '2.4-IV1',
                '2.5-IV0',
                '2.6-IV0',
                '2.7-IV0',
                '2.7-IV1',
                '2.7-IV2'
            ],
            'readOnly': true
        },
        'kafka.metrics.polling.interval.secs': {
            'default': '10',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'kafka.metrics.reporters': {
            'default': '',
            'mode': 'read-only',
            'readOnly': true
        },
        'leader.imbalance.check.interval.seconds': {
            'default': '300',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'leader.imbalance.per.broker.percentage': {
            'default': '10',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'leader.replication.throttled.rate': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'leader.replication.throttled.replicas': {
            'default': 'none',
            'mode': 'cluster-wide',
            'values': [
                'none',
                '*'
            ]
        },
        'listener.security.protocol.map': {
            'default': 'PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL',
            'mode': 'per-broker'
        },
        'listeners': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'log.cleaner.backoff.ms': {
            'default': '15000',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.dedupe.buffer.size': {
            'default': '134217728',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.delete.retention.ms': {
            'default': '86400000',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.cleaner.io.buffer.load.factor': {
            'default': '0.9',
            'mode': 'cluster-wide'
        },
        'log.cleaner.io.buffer.size': {
            'default': '524288',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.io.max.bytes.per.second': {
            'default': '1.7976931348623157E308',
            'mode': 'cluster-wide'
        },
        'log.cleaner.max.compaction.lag.ms': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.min.cleanable.ratio': {
            'default': '0.5',
            'mode': 'cluster-wide'
        },
        'log.cleaner.min.compaction.lag.ms': {
            'default': '0',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleaner.threads': {
            'default': '1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.cleanup.policy': {
            'default': 'delete',
            'mode': 'cluster-wide',
            'values': [
                'compact',
                'delete'
            ]
        },
        'log.deletion.max.segments.per.run': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.dir': {
            'default': '/tmp/kafka-logs',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.dirs': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.flush.interval.messages': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.flush.interval.ms': {
            'default': 'null',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.flush.offset.checkpoint.interval.ms': {
            'default': '60000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.flush.scheduler.interval.ms': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.flush.start.offset.checkpoint.interval.ms': {
            'default': '60000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.index.interval.bytes': {
            'default': '4096',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.index.size.max.bytes': {
            'default': '10485760',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.message.downconversion.enable': {
            'default': 'true',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'log.message.format.version': {
            'default': '2.7-IV2',
            'mode': 'read-only',
            'values': [
                '0.8.0',
                '0.8.1',
                '0.8.2',
                '0.9.0',
                '0.10.0-IV0',
                '0.10.0-IV1',
                '0.10.1-IV0',
                '0.10.1-IV1',
                '0.10.1-IV2',
                '0.10.2-IV0',
                '0.11.0-IV0',
                '0.11.0-IV1',
                '0.11.0-IV2',
                '1.0-IV0',
                '1.1-IV0',
                '2.0-IV0',
                '2.0-IV1',
                '2.1-IV0',
                '2.1-IV1',
                '2.1-IV2',
                '2.2-IV0',
                '2.2-IV1',
                '2.3-IV0',
                '2.3-IV1',
                '2.4-IV0',
                '2.4-IV1',
                '2.5-IV0',
                '2.6-IV0',
                '2.7-IV0',
                '2.7-IV1',
                '2.7-IV2'
            ],
            'readOnly': true
        },
        'log.message.timestamp.difference.max.ms': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.message.timestamp.type': {
            'default': 'CreateTime',
            'mode': 'cluster-wide',
            'values': [
                'CreateTime',
                'LogAppendTime'
            ]
        },
        'log.preallocate': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'log.retention.bytes': {
            'default': '-1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.retention.check.interval.ms': {
            'default': '300000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.retention.hours': {
            'default': '168',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.retention.minutes': {
            'default': 'null',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.retention.ms': {
            'default': 'null',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.roll.hours': {
            'default': '168',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.roll.jitter.hours': {
            'default': '0',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'log.roll.jitter.ms': {
            'default': 'null',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.roll.ms': {
            'default': 'null',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.segment.bytes': {
            'default': '1073741824',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'log.segment.delete.delay.ms': {
            'default': '60000',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'max.connection.creation.rate': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'max.connections': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'max.connections.per.ip': {
            'default': '2147483647',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'max.connections.per.ip.overrides': {
            'default': '',
            'mode': 'cluster-wide'
        },
        'max.incremental.fetch.session.cache.slots': {
            'default': '1000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'message.max.bytes': {
            'default': '1048588',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'metric.reporters': {
            'default': '',
            'mode': 'cluster-wide'
        },
        'metrics.num.samples': {
            'default': '2',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'metrics.recording.level': {
            'default': 'INFO',
            'mode': 'read-only',
            'readOnly': true
        },
        'metrics.sample.window.ms': {
            'default': '30000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'min.insync.replicas': {
            'default': '1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'num.io.threads': {
            'default': '8',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'num.network.threads': {
            'default': '3',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'num.partitions': {
            'default': '1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'num.recovery.threads.per.data.dir': {
            'default': '1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'num.replica.alter.log.dirs.threads': {
            'default': 'null',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'num.replica.fetchers': {
            'default': '1',
            'type': 'number',
            'mode': 'cluster-wide'
        },
        'offset.metadata.max.bytes': {
            'default': '4096',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.commit.required.acks': {
            'default': '-1',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.commit.timeout.ms': {
            'default': '5000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.load.buffer.size': {
            'default': '5242880',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.retention.check.interval.ms': {
            'default': '600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.retention.minutes': {
            'default': '10080',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.topic.compression.codec': {
            'default': '0',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.topic.num.partitions': {
            'default': '50',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.topic.replication.factor': {
            'default': '3',
            'mode': 'read-only',
            'readOnly': true
        },
        'offsets.topic.segment.bytes': {
            'default': '104857600',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.cipher.algorithm': {
            'default': 'AES/CBC/PKCS5Padding',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.iterations': {
            'default': '4096',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.key.length': {
            'default': '128',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.keyfactory.algorithm': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.old.secret': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'password.encoder.secret': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'port': {
            'default': '9092',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'principal.builder.class': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'producer.purgatory.purge.interval.requests': {
            'default': '1000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'queued.max.request.bytes': {
            'default': '-1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'queued.max.requests': {
            'default': '500',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'quota.consumer.default': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'quota.producer.default': {
            'default': '9223372036854775807',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'quota.window.num': {
            'default': '11',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'quota.window.size.seconds': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.fetch.backoff.ms': {
            'default': '1000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.fetch.max.bytes': {
            'default': '1048576',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.fetch.min.bytes': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.fetch.response.max.bytes': {
            'default': '10485760',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.fetch.wait.max.ms': {
            'default': '500',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.high.watermark.checkpoint.interval.ms': {
            'default': '5000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.lag.time.max.ms': {
            'default': '30000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.selector.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.socket.receive.buffer.bytes': {
            'default': '65536',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replica.socket.timeout.ms': {
            'default': '30000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replication.quota.window.num': {
            'default': '11',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'replication.quota.window.size.seconds': {
            'default': '1',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'request.timeout.ms': {
            'default': '30000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'reserved.broker.max.id': {
            'default': '1000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'sasl.client.callback.handler.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'sasl.enabled.mechanisms': {
            'default': 'GSSAPI',
            'mode': 'per-broker'
        },
        'sasl.jaas.config': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'sasl.kerberos.kinit.cmd': {
            'default': '/usr/bin/kinit',
            'mode': 'per-broker'
        },
        'sasl.kerberos.min.time.before.relogin': {
            'default': '60000',
            'type': 'number',
            'mode': 'per-broker'
        },
        'sasl.kerberos.principal.to.local.rules': {
            'default': 'DEFAULT',
            'mode': 'per-broker'
        },
        'sasl.kerberos.service.name': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'sasl.kerberos.ticket.renew.jitter': {
            'default': '0.05',
            'mode': 'per-broker'
        },
        'sasl.kerberos.ticket.renew.window.factor': {
            'default': '0.8',
            'mode': 'per-broker'
        },
        'sasl.login.callback.handler.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'sasl.login.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'sasl.login.refresh.buffer.seconds': {
            'default': '300',
            'mode': 'per-broker'
        },
        'sasl.login.refresh.min.period.seconds': {
            'default': '60',
            'mode': 'per-broker'
        },
        'sasl.login.refresh.window.factor': {
            'default': '0.8',
            'mode': 'per-broker'
        },
        'sasl.login.refresh.window.jitter': {
            'default': '0.05',
            'mode': 'per-broker'
        },
        'sasl.mechanism.inter.broker.protocol': {
            'default': 'GSSAPI',
            'mode': 'per-broker'
        },
        'sasl.server.callback.handler.class': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'security.inter.broker.protocol': {
            'default': 'PLAINTEXT',
            'mode': 'read-only',
            'readOnly': true
        },
        'security.providers': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'socket.connection.setup.timeout.max.ms': {
            'default': '127000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'socket.connection.setup.timeout.ms': {
            'default': '10000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'socket.receive.buffer.bytes': {
            'default': '102400',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'socket.request.max.bytes': {
            'default': '104857600',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'socket.send.buffer.bytes': {
            'default': '102400',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'ssl.cipher.suites': {
            'default': '',
            'mode': 'cluster-wide'
        },
        'ssl.client.auth': {
            'default': 'none',
            'mode': 'per-broker',
            'values': [
                'required',
                'requested',
                'none'
            ]
        },
        'ssl.enabled.protocols': {
            'default': 'TLSv1.2',
            'mode': 'per-broker'
        },
        'ssl.endpoint.identification.algorithm': {
            'default': 'https',
            'mode': 'per-broker'
        },
        'ssl.engine.factory.class': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.key.password': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.keymanager.algorithm': {
            'default': 'SunX509',
            'mode': 'per-broker'
        },
        'ssl.keystore.certificate.chain': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.keystore.key': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.keystore.location': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.keystore.password': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.keystore.type': {
            'default': 'JKS',
            'mode': 'per-broker'
        },
        'ssl.principal.mapping.rules': {
            'default': 'DEFAULT',
            'mode': 'read-only',
            'readOnly': true
        },
        'ssl.protocol': {
            'default': 'TLSv1.2',
            'mode': 'per-broker'
        },
        'ssl.provider': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.secure.random.implementation': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.trustmanager.algorithm': {
            'default': 'PKIX',
            'mode': 'per-broker'
        },
        'ssl.truststore.certificates': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.truststore.location': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.truststore.password': {
            'default': 'null',
            'mode': 'per-broker'
        },
        'ssl.truststore.type': {
            'default': 'JKS',
            'mode': 'per-broker'
        },
        'transaction.abort.timed.out.transaction.cleanup.interval.ms': {
            'default': '10000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.max.timeout.ms': {
            'default': '900000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.remove.expired.transaction.cleanup.interval.ms': {
            'default': '3600000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.state.log.load.buffer.size': {
            'default': '5242880',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.state.log.min.isr': {
            'default': '2',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.state.log.num.partitions': {
            'default': '50',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.state.log.replication.factor': {
            'default': '3',
            'mode': 'read-only',
            'readOnly': true
        },
        'transaction.state.log.segment.bytes': {
            'default': '104857600',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'transactional.id.expiration.ms': {
            'default': '604800000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'unclean.leader.election.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'cluster-wide'
        },
        'zookeeper.clientCnxnSocket': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.connect': {
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.connection.timeout.ms': {
            'default': 'null',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.max.in.flight.requests': {
            'default': '10',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.session.timeout.ms': {
            'default': '18000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.set.acl': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.cipher.suites': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.client.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.crl.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.enabled.protocols': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.endpoint.identification.algorithm': {
            'default': 'HTTPS',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.keystore.location': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.keystore.password': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.keystore.type': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.ocsp.enable': {
            'default': 'false',
            'type': 'boolean',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.protocol': {
            'default': 'TLSv1.2',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.truststore.location': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.truststore.password': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.ssl.truststore.type': {
            'default': 'null',
            'mode': 'read-only',
            'readOnly': true
        },
        'zookeeper.sync.time.ms': {
            'default': '2000',
            'type': 'number',
            'mode': 'read-only',
            'readOnly': true
        }
    },
    topicSettingList: {
        'cleanup.policy': {
            'default': 'delete',
            'multiple': true,
            'values': ['compact', 'delete']
        },
        'compression.type': {
            'default': 'producer',
            'values': ['uncompressed', 'zstd', 'lz4', 'snappy', 'gzip', 'producer']
        },
        'confluent.key.schema.validation': {
            'default': 'false',
            'type': 'boolean'
        },
        'confluent.key.subject.name.strategy': {
            'default': 'io.confluent.kafka.serializers.subject.TopicNameStrategy'
        },
        'confluent.placement.constraints': {
            'default': ''
        },
        'confluent.tier.enable': {
            'default': 'false',
            'type': 'boolean'
        },
        'confluent.tier.local.hotset.bytes': {
            'default': '-1',
            'type': 'number'
        },
        'confluent.tier.local.hotset.ms': {
            'default': '86400000',
            'type': 'number'
        },
        'confluent.value.schema.validation': {
            'default': 'false',
            'type': 'boolean'
        },
        'confluent.value.subject.name.strategy': {
            'default': 'io.confluent.kafka.serializers.subject.TopicNameStrategy'
        },
        'delete.retention.ms': {
            'default': '86400000',
            'type': 'number'
        },
        'file.delete.delay.ms': {
            'default': '60000',
            'type': 'number'
        },
        'flush.messages': {
            'default': '9223372036854775807',
            'type': 'number'
        },
        'flush.ms': {
            'default': '9223372036854775807',
            'type': 'number'
        },
        'follower.replication.throttled.replicas': {
            'default': ''
        },
        'index.interval.bytes': {
            'default': '4096',
            'type': 'number'
        },
        'leader.replication.throttled.replicas': {
            'default': ''
        },
        'max.compaction.lag.ms': {
            'default': '9223372036854775807',
            'type': 'number'
        },
        'max.message.bytes': {
            'default': '1048588',
            'type': 'number'
        },
        'message.downconversion.enable': {
            'default': 'true',
            'type': 'boolean'
        },
        'message.format.version': {
            'default': '2.7-IV2',
            'values': ['0.8.0', '0.8.1', '0.8.2', '0.9.0', '0.10.0-IV0', '0.10.0-IV1', '0.10.1-IV0', '0.10.1-IV1', '0.10.1-IV2', '0.10.2-IV0', '0.11.0-IV0', '0.11.0-IV1', '0.11.0-IV2', '1.0-IV0', '1.1-IV0', '2.0-IV0', '2.0-IV1', '2.1-IV0', '2.1-IV1', '2.1-IV2', '2.2-IV0', '2.2-IV1', '2.3-IV0', '2.3-IV1', '2.4-IV0', '2.4-IV1', '2.5-IV0', '2.6-IV0', '2.7-IV0', '2.7-IV1', '2.7-IV2']
        },
        'message.timestamp.difference.max.ms': {
            'default': '9223372036854775807',
            'type': 'number'
        },
        'message.timestamp.type': {
            'default': 'CreateTime',
            'values': ['CreateTime', 'LogAppendTime']
        },
        'min.cleanable.dirty.ratio': {
            'default': '0.5'
        },
        'min.compaction.lag.ms': {
            'default': '0',
            'type': 'number'
        },
        'min.insync.replicas': {
            'default': '1',
            'type': 'number'
        },
        'preallocate': {
            'default': 'false',
            'type': 'boolean'
        },
        'retention.bytes': {
            'default': '-1',
            'type': 'number'
        },
        'retention.ms': {
            'default': '604800000',
            'type': 'number'
        },
        'segment.bytes': {
            'default': '1073741824',
            'type': 'number'
        },
        'segment.index.bytes': {
            'default': '10485760',
            'type': 'number'
        },
        'segment.jitter.ms': {
            'default': '0',
            'type': 'number'
        },
        'segment.ms': {
            'default': '604800000',
            'type': 'number'
        },
        'unclean.leader.election.enable': {
            'default': 'false',
            'type': 'boolean'
        }
    }
};