# Deploy Streaming Platform (Apache Kafka)

To deploy Apache Kafka as a persistence broker for our Event-Driven Architectures execute:

```shell
oc apply -f kafka/configmap/
oc apply -f kafka/event-bus-kafka.yml
```

After some minutes we should have our Apache Kafka cluster deployed. We could check as:

```shell
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

```shell
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

The Kafka cluster is accessible from outside of OpenShift using a route created on top
of the `event-bus-kafka-external-bootstrap` service. This route requires use the CA created by
the operator. 

To extract the CA and import into a JKS trust store file: 

```shell
❯ oc extract secret/event-bus-cluster-ca-cert --keys=ca.crt --to=- > ca.crt
❯ keytool -import -trustcacerts -alias root -file ca.crt -keystore truststore.jks -storepass password -noprompt
```

## Rolling Updates

To update Zookeeper StatefulSet without downtime use the following command:following command:

```shell
oc annotate statefulset event-bus-zookeeper strimzi.io/manual-rolling-update=true
```

To update Kafka StatefulSet without downtime use the 

```shell
oc annotate statefulset event-bus-kafka strimzi.io/manual-rolling-update=true
```

## Deploying KafkaTopics

## Data KafkaTopics

Set of topics to store base data of our solution.

```shell
oc apply -f topics/data
```

## Domain KafkaTopics

Set of topics to store aggregated data in our Domain-Driven Design model.

```shell
oc apply -f topics/domain
```

## Events KafkaTopics

Set of topics to store common or general events of our solution.

```shell
oc apply -f topics/events
```

## Clean up KafkaTopics

To delete the `KafkaTopic` created to build a fresh installation:

```shell
oc delete kt eda.data.accounts
oc delete kt eda.data.clients 
oc delete kt eda.data.movements
oc delete kt eda.data.regions  
oc delete kt eda.events.aggregate-metrics
oc delete kt eda.events.alerts           
oc delete kt eda.events.domain.accounts  
oc delete kt eda.events.domain.accounts.balance.low.reached
oc delete kt eda.events.domain.accounts.balance.neg.reached
oc delete kt eda.events.domain.accounts.balance.vip.reached
oc delete kt eda.events.domain.clients                     
oc delete kt eda.events.domain.clients.accounts.closed     
oc delete kt eda.events.domain.clients.accounts.inactivated
oc delete kt eda.events.domain.regions                     
```

To delete the `KafkaTopic` created by the Kafka Streams API:

```shell
oc delete kt quarkus-streaming-eda.data.accounts-state-store-0000000003-changelog---5346c3863458ac3ee7a6d862c28f20219bdc1f10             
oc delete kt quarkus-streaming-eda.data.clients-state-store-0000000000-changelog---b8a91c7bd55e206c46a08e94373362aecdc1fee9              
oc delete kt quarkus-streaming-eda.data.movements-state-store-0000000030-changelog---d51e50ab371c5b9356947f95b79cbfc865b5d248            
oc delete kt quarkus-streaming-eda.data.regions-state-store-0000000006-changelog---7d54c9b21b6b6c2624260644555fd474462f2b7c              
oc delete kt quarkus-streaming-eda.events.domain.accounts-state-store-0000000054-changelog---a343325afebbb850b4baf391871911d22df41578    
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000024-changelog---800b8e51ac96daf78ac24c310b47c583a1ea5db2              
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000024-repartition---1351fafdda448d7d429256845eeb945c4bcb83d1            
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000048-changelog---93678f35965c4692035f16d3e7e9919cc7a30a00              
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000048-repartition---1c1fa6ba3921bb14745674022bba347bf9265e0e            
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000072-changelog---b838f1132701ccd5d833d5cad6c7be28ec528db3              
oc delete kt quarkus-streaming-ktable-aggregate-state-store-0000000072-repartition---9322d5dbc0563dc86e8d00f79eb2e8ffa6ec1c95            
oc delete kt quarkus-streaming-ktable-fk-join-output-state-store-0000000022-changelog---9c811a4db3030ede984992876de6874b1c88d424         
oc delete kt quarkus-streaming-ktable-fk-join-output-state-store-0000000046-changelog---6bf4a1b3a9614518c2bc21e147c4d1c205157582         
oc delete kt quarkus-streaming-ktable-fk-join-output-state-store-0000000070-changelog---df58d110f2c02c77b66b40be213b1938e44ead96         
oc delete kt quarkus-streaming-ktable-fk-join-subscription-registration-0000000009-topic---3bfbb82bcf9714a279133034baa699f4d752908c      
oc delete kt quarkus-streaming-ktable-fk-join-subscription-registration-0000000033-topic---ea09ecc54edac906f03dfa82ee82fef51157c362      
oc delete kt quarkus-streaming-ktable-fk-join-subscription-registration-0000000057-topic---8b2857af4e7cbb9a0baab0df2d6f9f5ce27c723c      
oc delete kt quarkus-streaming-ktable-fk-join-subscription-response-0000000017-topic---3fc9cc56ca7498fed9d55a18679cc42871061a76          
oc delete kt quarkus-streaming-ktable-fk-join-subscription-response-0000000041-topic---6c374b79304564dbcee31d750dfcdc93b7bc102f          
oc delete kt quarkus-streaming-ktable-fk-join-subscription-response-0000000065-topic---6a4f9a1727ae72a73181a8ff524ffd9382a7c3ff          
oc delete kt quarkus-streaming-ktable-fk-join-subscription-state-store-0000000013-changelog---43e3e6cdb9c905eea6a0015944a03093dfc20dc9   
oc delete kt quarkus-streaming-ktable-fk-join-subscription-state-store-0000000037-changelog---672fe9879f3f126c94f0be3e215dc1519bf1e573   
oc delete kt quarkus-streaming-ktable-fk-join-subscription-state-store-0000000061-changelog---2e51bc24d0f2e790f0fc07104758d9d07d1c0392   
```

**NOTE**: The serial id suffix could change in your environment.


