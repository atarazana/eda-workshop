# Deploy Streaming Platform (Apache Kafka)

To deploy Apache Kafka as a persistence broker for our Event-Driven Architectures execute:

```shell script
oc apply -f kafka/configmap/
oc apply -f kafka/event-bus-kafka.yml
```

After some minutes we should have our Apache Kafka cluster deployed. We could check as:

```shell script
❯ oc get k event-bus -o yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: Kafka
# ...
status:
  clusterId: QysSEfizSGu6-JKyRge_RA
  conditions:
  - lastTransitionTime: "2021-06-03T07:44:50.140Z"
    status: "True"
    type: Ready
# ...    
```

This Apache Kafka cluster will be available by the `event-bus-kafka-bootstrap` service:

```shell script
❯ oc get svc
NAME                                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
event-bus-cruise-control             ClusterIP   172.30.178.143   <none>        9090/TCP                     108s
event-bus-kafka-0                    ClusterIP   172.30.232.201   <none>        9094/TCP                     4m32s
event-bus-kafka-1                    ClusterIP   172.30.224.92    <none>        9094/TCP                     4m32s
event-bus-kafka-2                    ClusterIP   172.30.110.118   <none>        9094/TCP                     4m32s
event-bus-kafka-bootstrap            ClusterIP   172.30.154.88    <none>        9091/TCP,9092/TCP,9093/TCP   4m32s
event-bus-kafka-brokers              ClusterIP   None             <none>        9091/TCP,9092/TCP,9093/TCP   4m32s
event-bus-kafka-external-bootstrap   ClusterIP   172.30.123.176   <none>        9094/TCP                     4m32s
event-bus-zookeeper-client           ClusterIP   172.30.39.80     <none>        2181/TCP                     6m49s
event-bus-zookeeper-nodes            ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP   6m49s
```

## Deploying KafkaTopics

## Data KafkaTopics

Set of topics to store base data of our solution.

```shell script
oc apply -f topics/data
```

## Domain KafkaTopics

Set of topics to store aggregated data in our Domain-Driven Design model.

```shell script
oc apply -f topics/domain
```

## Events KafkaTopics

Set of topics to store common or general events of our solution.

```shell script
oc apply -f topics/events
```
