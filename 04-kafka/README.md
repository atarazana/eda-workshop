# Deploy Streaming Platform (Apache Kafka)

To deploy Apache Kafka as a persistence broker for our Event-Driven Architectures execute:

```shell script
oc apply -f kafka/configmap/
oc apply -f kafka/event-bus-kafka.yml
```
