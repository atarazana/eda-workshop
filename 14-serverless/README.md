# Knative Serverless

This section of the architecture is based in Serverless. We will deploy the different
services of the solution as Knative Services, based in Red Hat OpenShift Serverless.

Before to deploy the new services, we will scale down the current applications:

```shell
oc scale dc/data-streaming --replicas=0
oc scale dc/backend --replicas=0
oc scale dc/dashboard --replicas=0
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
NAME                                     READY   STATUS    RESTARTS   AGE
activator-5ccb9bf477-5nz9d               2/2     Running   0          21h
activator-5ccb9bf477-zmd9n               2/2     Running   0          21h
autoscaler-58b6d67565-dz9t4              2/2     Running   0          21h
autoscaler-58b6d67565-qvgtz              2/2     Running   0          21h
autoscaler-hpa-6554cfc674-8kgqh          2/2     Running   0          21h
autoscaler-hpa-6554cfc674-hdxdw          2/2     Running   0          21h
controller-67fcf5575c-7kn44              2/2     Running   0          21h
controller-67fcf5575c-ng68h              2/2     Running   0          21h
domain-mapping-77b6c5ff4b-r9lcw          2/2     Running   0          21h
domain-mapping-77b6c5ff4b-x62vw          2/2     Running   0          21h
domainmapping-webhook-755b79747d-5ljjv   2/2     Running   0          21h
domainmapping-webhook-755b79747d-l8s4t   2/2     Running   0          21h
webhook-55c4b97656-rhtk7                 2/2     Running   0          21h
webhook-55c4b97656-zkzw5                 2/2     Running   0          21h
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
NAME                                                  READY   STATUS      RESTARTS   AGE
eventing-controller-7fc87788d-mpfwd                   2/2     Running     0          21h
eventing-controller-7fc87788d-vswts                   2/2     Running     0          21h
eventing-webhook-64b9965c69-drbrc                     2/2     Running     0          21h
eventing-webhook-64b9965c69-f4d5l                     2/2     Running     0          21h
imc-controller-544d8cd554-knss2                       2/2     Running     0          21h
imc-controller-544d8cd554-xtwh9                       2/2     Running     0          21h
imc-dispatcher-577774f87d-67kxn                       2/2     Running     0          21h
imc-dispatcher-577774f87d-l8l8l                       2/2     Running     0          21h
kafka-channel-dispatcher-7b4755bff5-5sngt             2/2     Running     0          21h
kafka-channel-receiver-6f96c865d6-mjg7s               2/2     Running     0          21h
kafka-controller-794497b975-97ngb                     2/2     Running     0          21h
kafka-controller-post-install-1.28.0-sbmjp            0/1     Completed   0          8m44s
kafka-source-dispatcher-0                             2/2     Running     0          3h34m
kafka-webhook-eventing-64999d8559-sp475               2/2     Running     0          21h
knative-kafka-storage-version-migrator-1.28.0-2t8md   0/1     Completed   0          6m23s
mt-broker-controller-5b48b69554-6bnq5                 2/2     Running     0          21h
mt-broker-controller-5b48b69554-zsld9                 2/2     Running     0          21h
mt-broker-filter-65fb8fb459-fg4vj                     2/2     Running     0          21h
mt-broker-filter-65fb8fb459-n5kpb                     2/2     Running     0          21h
mt-broker-ingress-6cfd4ff67-hn487                     2/2     Running     0          21h
mt-broker-ingress-6cfd4ff67-zqhxh                     2/2     Running     0          21h
```

Deploy sample KafkaTopic for testing proposal:

```shell
oc apply -f knative-eventing/topics
```

## Deploying Application Serverless Services

To deploy our Application Serverless Services:

```shell
oc apply -f service/
```

You could check the Application Serverless Services

```shell
❯ kn service list
NAME                        URL                                                                      LATEST                            AGE    CONDITIONS   READY
backend-serverless          http://backend-serverless-eda-workshop.apps.sandbox.opentlc.com          backend-serverless-00001          98s    3 OK / 3     True    
dashboard-serverless        http://dashboard-serverless-eda-workshop.apps.sandbox.opentlc.com        dashboard-serverless-00001        98s    3 OK / 3     True    
data-streaming-serverless   http://data-streaming-serverless-eda-workshop.apps.sandbox.opentlc.com   data-streaming-serverless-00001   98s    3 OK / 3     True    
event-display-serverless    http://event-display-serverless-eda-workshop.apps.sandbox.opentlc.com    event-display-serverless-00001    113s   3 OK / 3     True
```

Deploy Kafka Sources to consume messages from Kafka to start up the services

```shell
oc apply -f knative-eventing/kafka-source
```

You could check the `KafkaSource` objects:

```shell
❯ oc get kafkasource
NAME                          TOPICS                                                                                                                                               BOOTSTRAPSERVERS                                      READY   REASON   AGE
backend-kafka-source          ["eda.data.accounts","eda.data.clients","eda.data.movements","eda.events.domain.clients","eda.events.domain.accounts","eda.events.domain.regions"]   ["event-bus-kafka-bootstrap.eda-workshop.svc:9092"]   True             4m26s
data-streaming-kafka-source   ["dbserver02.enterprise.accounts","dbserver02.enterprise.clients","dbserver02.enterprise.movements","dbserver02.enterprise.regions"]                 ["event-bus-kafka-bootstrap.eda-workshop.svc:9092"]   True             4m26s
event-display-kafka-source    ["eda.cloud.events"]                                                                                                                                 ["event-bus-kafka-bootstrap.eda-workshop.svc:9092"]   True             4m26s
```

Now every time a new message is sent to `eda.cloud.events` topic or `eda.events.alerts` or `eda.events.aggregate-metrics`, the message
will be processed by a `event-display` instance or the dashboard-serverless instance:

To send messages to events topic

```shell
❯ oc run eda-serverless-kafka-producer -ti \
 --image=registry.redhat.io/amq7/amq-streams-kafka-31-rhel8:2.1.0 \
 --rm=true --restart=Never \
 -- bin/kafka-console-producer.sh \
 --broker-list event-bus-kafka-bootstrap.eda-workshop.svc:9092 \
 --topic eda.cloud.events
If you don't see a command prompt, try pressing enter.
>Hello Serverless World from Apache Kafka!
```

Checking that the event was processed by the `event-display` service:

```shell
❯ oc logs -c user-container -f event-display-64vkz-deployment-6dd7676664-6j7sz
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
kn service update backend-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/backend:2.10.1-SNAPSHOT
```

To update `data-streaming-serverless` service:

```shell
kn service update data-streaming-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/data-streaming:2.10.1-SNAPSHOT
```

## Main References

[Installing Knative Serving](https://docs.openshift.com/container-platform/4.10/serverless/install/installing-knative-serving.html)
[Installing Knative Eventing](https://docs.openshift.com/container-platform/4.10/serverless/install/installing-knative-eventing.html)
