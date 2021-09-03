# KafkaManager

This is a standalone docker image which contains frontend (Angular) and backend (Spring Boot app) parts.

Depending your needs and system you may want to provide additional services such as:
- Database: Required when you need to connect to multiple Kafka cluster
- Elasticsearch: Required if you want to keep mettrics for longer periods. Without it, metrics are kept for the user session duration (ie, the time you are logged into the application)

## Run the container

To start the Kafka manager application, you must run the docker image with several parameters which are described bellow.

For information, this docker image contains all application parts, ie, frontend (Angular website) and backend (Spring Boot application).  

> The communication between the front and the back is managed inside the docker container.

### Example to start docker container

In this example, we will launch the "KafkaManager" application in "Restricted mode"

```
docker run -d --name kafka-manager -p 8001:8001 -v /tmp/kafka-manager:/srv/log -e "JAVA_OPTS=-Dcluster.name=localCluster -Dcluster.kafkaVersion=2.4.2 -Dcluster.zkAddr=127.0.0.1:2181 -Dcluster.brokerAddrs[0].address=127.0.0.1 -Dcluster.brokerAddrs[0].kafkaPort=9092 -Dcluster.brokerAddrs[0].jmxPort=9093" telix/wl-kafka-manager:latest
```

In this example, we will launch the "KafkaManager" application in "MySQL mode"

```
docker run -d --name kafka-manager -p 8001:8001 -v /tmp/kafka-manager:/srv/log -e "JAVA_OPTS=-Ddatabase.enable=true -Dspring.datasource.url=jdbc:mysql://127.0.0.1:3306/kafka_manager?serverTimezone=Europe/Paris -Dspring.datasource.username=root -Dspring.datasource.password=root" telix/wl-kafka-manager:latest
```

### General options

* __logging.level.com.worldline.kafka.kafkamanager__=DEBUG

### Database options (default values)

* __-Ddatabase.enable__ = true
* __-Dspring.datasource.url__ = jdbc:mysql://127.0.0.1:3306/kafka_manager?serverTimezone=Europe/Paris
* __-Dspring.datasource.username__ = root
* __-Dspring.datasource.password__ = root
* __elasticsearch.enable__ = true

### Restricted mode options (Without database)

* __-Ddatabase.enable__ = false
* __-Dcluster.name__ = "_YOUR_CLUSTER_NAME_"
* __-Dcluster.kafkaVersion__ = "_YOUR_KAFKA_VERSION_" (Optional)
* __-Dcluster.zkAddr__ = "_YOUR_ZOOKEEPER_ADDR_"
* __-Dcluster.brokerAddrs[X].address__ = "_YOUR_BROKER_ADDR_"
* __-Dcluster.brokerAddrs[X].kafkaPort__ = "_YOUR_BROKER_PORT_"
* __-Dcluster.brokerAddrs[X].jmxPort__ = "_YOUR_BROKER_JMX_PORT_"

### Kafka Options  (Default value)

* __kafka.consumerservice.poll.duration__="500" #in ms

Example with override (5Sec)

### Authentication 
Three authenticiation modes are supported:

#### JWT Configuration
* __authentication.jwt.expiration__= 14400
* __authentication.jwt.secret__= "_YOUR_BASE64_SECRET_"

#### OpenId auth

* __authentication.open-id.url__= "_URL_OPENID_CONNECT_"
* __authentication.open-id.clientId__= "_YOUR_REALME_"
* __authentication.open-id.secret__= "_YOUR_SECRET_"
* __authentication.open-id.adminRoleMapping__= "_ADMIN_ROLE_MAPPING_"
* __authentication.open-id.adminUsernameMapping__= "ADMIN_IDS"
* __authentication.open-id.adminWhiteList__= "ADMIN_IDS"

#### InMemory auth

* __authentication.in-memory.enabled__=false
* __authentication.in-memory.userLogin__= "user"
* __authentication.in-memory.userPasswordHash__= "_USER_BCRYPT_HASH_PWD_"
* __authentication.in-memory.adminLogin__= "admin"
* __authentication.in-memory.adminPasswordHash__= "_ADMIN_BCRYPT_HASH_PWD_"

### ElasticSearch Options (Default values)

* __-Delasticsearch.enable__= false
* __elasticsearch.url__= "_SERVER_:_PORT_"

### Metrics / Monitoring  options (Default values)

#### Metrics

* __-Dmetrics.enable__ = true
* __-Dmetrics.schedule__ = true
* __-Dmetrics.cron-async-storage__ = 0 */1 * * * *
* __-Dmetrics.clean.cron-clean-storage__ = 0 */1 * * * *
* __-Dmetrics.clean.clean-time__ = 30

#### Monitoring

* __-Dmonitoring.enable__= true

### Event Options

* __-Devent.clean.cron-clean-storage__ = 0 */1 * * * *
* __-Devent.clean.clean-time__ = 30

### Kafka Connect

* __-Dkafka-connect.enable__ = true

## Credits

Original project from FS Lab 2021

### Authors

* Romain Thely
* Baptiste Coulaud
* Flavien Flandrin
* Elliot Humbert