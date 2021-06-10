# Knative Serverless

This section of the architecture is based in Serverless. We will deploy the different
services of the solution as Knative Services, based in Red Hat OpenShift Serverless.

Before to deploy the new services, we will scale down the current applications:

```shell
oc scale dc/data-streaming --replicas=0
oc scale dc/backend --replicas=0
```

## Deploying Serving Services

To deploy the Knative Serving services:

```shell
oc create -f knative-serving/knative-serving.yaml
```

To verify that Knative Serving Services are available:

```shell
❯ oc get knativeserving.operator.knative.dev/knative-serving -n knative-serving --template='{{range .status.conditions}}{{printf "%s=%s\n" .type .status}}{{end}}'
DependenciesInstalled=True
DeploymentsAvailable=True
InstallSucceeded=True
Ready=True
VersionMigrationEligible=True
```

Also you could check the pods in `knative-serving` namespace:

```shell
❯ oc get pods -n knative-serving
activator-7dff495b5c-bk2t8                               2/2     Running     0          3d
activator-7dff495b5c-v6qxm                               2/2     Running     0          3d
autoscaler-5946b4756c-28btt                              2/2     Running     0          3d
autoscaler-hpa-d4b9d7988-5kwvh                           2/2     Running     0          3d
autoscaler-hpa-d4b9d7988-hzsh2                           2/2     Running     0          3d
controller-5794c5654f-b2jvr                              2/2     Running     0          6h55m
controller-5794c5654f-ws7kv                              2/2     Running     0          6h55m
domain-mapping-79d766ccbc-58kv4                          2/2     Running     0          3d
domainmapping-webhook-86d5d6b94c-zmhcd                   2/2     Running     0          3d
storage-version-migration-serving-serving-0.20.0-xxd96   0/1     Completed   0          3d
webhook-bb8ccfd57-b5788                                  2/2     Running     0          3d
webhook-bb8ccfd57-cfbqs                                  2/2     Running     0          3d
```

## Deploying Eventing Services

To deploy the Knative Serving services:

```shell
oc create -f knative-eventing/knative-eventing.yaml
```

To verify that Knative Eventing Services are available:

```shell
❯ oc get knativeeventing.operator.knative.dev/knative-eventing -n knative-eventing --template='{{range .status.conditions}}{{printf "%s=%s\n" .type .status}}{{end}}'
DependenciesInstalled=True
DeploymentsAvailable=True
InstallSucceeded=True
Ready=True
VersionMigrationEligible=True
```

Enable Serverless Eventing to manage Kafka cluster deploying a `KnativeKafka` definition using
the current Apache Kafka cluster deployed:

```shell
oc apply -f knative-eventing/knative-kafka.yaml -n knative-eventing
```

Also you could check the pods in `knative-eventing` namespace:

```shell
❯ oc get pods -n knative-eventing
eventing-controller-7b9cdbf9cf-s94zk        1/1     Running   0          15h
eventing-webhook-b8f8cdc96-wf6bz            1/1     Running   0          15h
imc-controller-55dc57b4d-llgc2              1/1     Running   0          15h
imc-dispatcher-79f447d6f-rckcx              1/1     Running   0          15h
kafka-ch-controller-fd859cc5f-48mzk         1/1     Running   0          15h
kafka-ch-dispatcher-649945777d-tqw9q        1/1     Running   0          15h
kafka-controller-manager-79644f667f-dtwqv   1/1     Running   0          15h
kafka-webhook-68db549d7-t8f9q               1/1     Running   0          15h
mt-broker-controller-55d65b468b-5gw6b       1/1     Running   0          15h
mt-broker-filter-7c8ff49f98-pvxfn           1/1     Running   0          15h
mt-broker-ingress-5458f4c5bc-gnhk5          1/1     Running   0          15h
sugar-controller-5f6fb848b8-fzx9w           1/1     Running   0          15h
```

Deploy sample KafkaTopic for testing proposal:

```shell
oc apply -f knative-eventing/knative-kafka/topics
```

## Deploying Application Serverless Services

To deploy our Application Serverless Services:

```shell
oc apply -f service/data-streaming-service.yaml
oc apply -f service/backend-service.yaml
oc apply -f service/event-display-service.yaml
```

You could check the Application Serverless Services

```shell
❯ kn service list
NAME                        URL                                                                               LATEST                            AGE   CONDITIONS   READY   REASON
backend-serverless          http://backend-serverless-eda-workshop.apps.labs.sandbox1754.opentlc.com          backend-serverless-00001          17h   3 OK / 3     True    
data-streaming-serverless   http://data-streaming-serverless-eda-workshop.apps.labs.sandbox1754.opentlc.com   data-streaming-serverless-00001   16h   3 OK / 3     True    
event-display-serverless    http://event-display-serverless-eda-workshop.apps.labs.sandbox1754.opentlc.com    event-display-serverless-00001    69s   3 OK / 3     True    
```

Deploy Kafka Sources to consume messages from Kafka to start up the services

```shell
oc apply -f knative-eventing/kafka-source
```

You could check the `KafkaSource` objects:

```shell
❯ oc get kafkasource
NAME                          TOPICS                                                                                                                                               BOOTSTRAPSERVERS                                      READY   REASON   AGE
backend-kafka-source          ["eda.data.accounts","eda.data.clients","eda.data.movements","eda.events.domain.clients","eda.events.domain.accounts","eda.events.domain.regions"]   ["event-bus-kafka-bootstrap.eda-workshop.svc:9092"]   True             17h
data-streaming-kafka-source   ["dbserver02.enterprise.accounts","dbserver02.enterprise.clients","dbserver02.enterprise.movements","dbserver02.enterprise.regions"]                 ["event-bus-kafka-bootstrap.eda-workshop.svc:9092"]   True             16h
```

Now every time a new message is sent to `eda.cloud.events` topic or `eda.events.alerts` or `eda.events.aggregate-metrics`, the message will be processed by a `event-display` instance or the dashboard-serverless instance:

To send messages to events topic

```shell
❯ oc run eda-serverless-kafka-producer -ti \
 --image=registry.redhat.io/amq7/amq-streams-kafka-27-rhel7:1.7.0 \
 --rm=true --restart=Never \
 -- bin/kafka-console-producer.sh \
 --broker-list event-bus-kafka-bootstrap.eda-workshop.svc:9092 \
 --topic eda.cloud.events
If you don't see a command prompt, try pressing enter.
>Hello Serverless World from Apache Kafka!
```

Checking that the event was processed by the `event-display` service:

```shell
❯ oc logs -f event-display-64vkz-deployment-6dd7676664-6j7sz -c user-container
☁️  cloudevents.Event
Validation: valid
Context Attributes,
  specversion: 1.0
  type: dev.knative.kafka.event
  source: /apis/v1/namespaces/knative-demo-eventing/kafkasources/kafka-source#events
  subject: partition:0#1
  id: partition:0/offset:1
  time: 2021-02-03T14:46:48.376Z
Extensions,
  traceparent: 00-58cddc96c8c32beb14f2f1b84afdc144-61fe1778e98d1cd3-00
Data,
  Hello Serverless World from Apache Kafka!
```

## Updating Service Image Version

Once the Knative Service is deployed the image tag used will be fixed to the current at that moment.
If we would like to update to the latest version of the image of the application, then you should
execute the following commands:

To update `backend-serverless` service:

```shell
kn service update backend-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/backend:1.0.0-SNAPSHOT
```

To update `data-streaming-serverless` service:

```shell
kn service update data-streaming-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/data-streaming:1.0.0-SNAPSHOT
```

## Main References

[Installing Knative Serving](https://docs.openshift.com/container-platform/4.7/serverless/admin_guide/installing-knative-serving.html)
[Installing Knative Eventing](https://docs.openshift.com/container-platform/4.7/serverless/admin_guide/installing-knative-eventing.html)
