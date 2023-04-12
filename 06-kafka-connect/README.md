# Deploying Kafka Connect Cluster

To deploy Kafka Connect requires create a initial `ImageStream` of the container image:

```shell
oc apply -f eda-kafka-connect-is.yaml 
```

It is needed because the KafkaConnect cluster will be built adding the Debezium Connectors
using the build capabilities of Red Hat AMQ Streams.

To deploy Kafka Connect cluster exposing metrics:

```shell
❯ oc apply -f topics/
❯ oc apply -f configmap/
❯ oc apply -f eda-kafka-connect.yaml
```

After some minutes the KafkaConnect Cluster will be ready:

```shell
❯ oc get kafkaconnect eda-kafka-connect -o yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaConnect
# ...
spec:
# ...
status:
  conditions:
  - lastTransitionTime: "2023-04-12T10:30:22.126914Z"
    status: "True"
    type: Ready
  connectorPlugins:
  - class: io.debezium.connector.mysql.MySqlConnector
    type: source
    version: 1.9.7.Final-redhat-00003
  - class: io.debezium.connector.postgresql.PostgresConnector
    type: source
    version: 1.9.7.Final-redhat-00003
  - class: org.apache.kafka.connect.mirror.MirrorCheckpointConnector
    type: source
    version: 3.3.1.redhat-00008
  - class: org.apache.kafka.connect.mirror.MirrorHeartbeatConnector
    type: source
    version: 3.3.1.redhat-00008
  - class: org.apache.kafka.connect.mirror.MirrorSourceConnector
    type: source
    version: 3.3.1.redhat-00008
  labelSelector: strimzi.io/cluster=eda-kafka-connect,strimzi.io/name=eda-kafka-connect-connect,strimzi.io/kind=KafkaConnect
  observedGeneration: 3
  replicas: 1
  url: http://eda-kafka-connect-connect-api.eda-workshop.svc:8083
```

The `status` lists the different KafkaConnectors available to use in this KafkaConnect cluster.

## Deploying KafkaConnector Samples

To test and verify that KafkaConnect cluster is ready, we will deploy a simple source KafkaConnector:

```shell
oc apply -f connectors/
```

To list the current KafkaConnectors:

```shell
❯ oc get kafkaconnectors
NAME                    CLUSTER             CONNECTOR CLASS                                           MAX TASKS   READY
file-source-connector   eda-kafka-connect   org.apache.kafka.connect.file.FileStreamSourceConnector   1           True
```

If this KafkaConnector run successfully a new KafkaTopic should be created

```shell
❯ oc get kt
NAME                                CLUSTER     PARTITIONS   REPLICATION FACTOR   READY
samples.connect.file-source-topic   event-bus   1            3                    True
```

## Deploying Debezium Connectors

To deploy the Debezium Connectors for MySQL to get the events from the MySQL Databases:

```shell
oc apply -f debezium-mysql/
```

To check the status of the current `KafkaConnectors` deployed:

```shell
❯ oc get kafkaconnector
NAME                                CLUSTER             CONNECTOR CLASS                                           MAX TASKS   READY
file-source-connector               eda-kafka-connect   org.apache.kafka.connect.file.FileStreamSourceConnector   1           True
mysql-enterprise-source-connector   eda-kafka-connect   io.debezium.connector.mysql.MySqlConnector                1           True
mysql-inventory-source-connector    eda-kafka-connect   io.debezium.connector.mysql.MySqlConnector                1           True
```

To verify that all the KafkaTopics from each MySQL table is created:

```shell
❯ oc get kt
NAME                                                                              CLUSTER     PARTITIONS   REPLICATION FACTOR   READY
dbserver01                                                                        event-bus   1            3                    True
dbserver01.inventory.addresses                                                    event-bus   1            3                    True
dbserver01.inventory.customers                                                    event-bus   1            3                    True
dbserver01.inventory.geom                                                         event-bus   1            3                    True
dbserver01.inventory.orders                                                       event-bus   1            3                    True
dbserver01.inventory.products                                                     event-bus   1            3                    True
dbserver01.inventory.products-on-hand---6a8b200cccca29a0d62d59204506736ec1b3197   event-bus   1            3                    True
dbserver02.enterprise.regions                                                     event-bus   1            3                    True
schema-changes.enterprise                                                         event-bus   1            3                    True
schema-changes.inventory                                                          event-bus   1            3                    True
```

## Restarting a KafkaConnector

To restart a KafkaConnector:

```shell
oc annotate kafkaconnector mysql-enterprise-source-connector strimzi.io/restart=true
```
