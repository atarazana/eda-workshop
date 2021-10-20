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
NAME                                                     READY   STATUS      RESTARTS   AGE
activator-f68c6fb67-krm9n                                2/2     Running     0          100s
activator-f68c6fb67-sgfrr                                2/2     Running     0          86s
autoscaler-98c9ffc79-6p2fg                               2/2     Running     0          69s
autoscaler-98c9ffc79-zcv5k                               2/2     Running     0          100s
autoscaler-hpa-6779db9d98-l7n2s                          2/2     Running     0          97s
autoscaler-hpa-6779db9d98-pgzvn                          2/2     Running     0          97s
controller-6c45ff7df8-c758h                              2/2     Running     0          67s
controller-6c45ff7df8-rjmmv                              2/2     Running     0          91s
domain-mapping-566454db5c-bgqdb                          2/2     Running     0          96s
domainmapping-webhook-668d945dff-8kdmr                   2/2     Running     0          96s
kn-cli-00001-deployment-85fdb79d4-lhxjv                  2/2     Running     0          47s
kn-cli-00002-deployment-9f84df9b4-9r9nq                  2/2     Running     0          47s
storage-version-migration-serving-serving-0.21.0-6tkmc   0/1     Completed   0          95s
webhook-56f6d98499-9tb2f                                 2/2     Running     0          84s
webhook-56f6d98499-fnb74                                 2/2     Running     0          99s
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
NAME                                          READY   STATUS      RESTARTS   AGE
eventing-controller-6ddfbbb6b9-8xmp7          2/2     Running     0          2m13s
eventing-webhook-6c8ccf95b6-72ljw             2/2     Running     0          2m12s
imc-controller-56dbdcf559-7k6j2               2/2     Running     0          2m8s
imc-dispatcher-747c575f59-z5jjh               2/2     Running     0          2m7s
kafka-ch-controller-854d74bc7f-fztfq          1/1     Running     0          32s
kafka-controller-manager-7dd57996c-8tlsk      1/1     Running     0          31s
kafka-webhook-775dcbd549-875p2                1/1     Running     0          32s
mt-broker-controller-5bb8459547-25xnf         2/2     Running     0          2m6s
mt-broker-filter-79cf4cd577-57lwd             2/2     Running     0          2m7s
mt-broker-ingress-868f47567c-fm5zf            2/2     Running     0          2m6s
sugar-controller-6cf9545794-bsqkd             2/2     Running     0          2m6s
v0.21-kafka-storage-version-migration-tl687   0/1     Completed   0          31s
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
NAME                        URL                                                                                           LATEST                            AGE    CONDITIONS   READY   REASON
backend-serverless          http://backend-serverless-eda-workshop.apps.cluster-1498.1498.sandbox380.opentlc.com          backend-serverless-00001          98s    3 OK / 3     True    
dashboard-serverless        http://dashboard-serverless-eda-workshop.apps.cluster-1498.1498.sandbox380.opentlc.com        dashboard-serverless-00001        98s    3 OK / 3     True    
data-streaming-serverless   http://data-streaming-serverless-eda-workshop.apps.cluster-1498.1498.sandbox380.opentlc.com   data-streaming-serverless-00001   98s    3 OK / 3     True    
event-display-serverless    http://event-display-serverless-eda-workshop.apps.cluster-1498.1498.sandbox380.opentlc.com    event-display-serverless-00001    113s   3 OK / 3     True
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

Now every time a new message is sent to `eda.cloud.events` topic or `eda.events.alerts` or `eda.events.aggregate-metrics`, the message will be processed by a `event-display` instance or the dashboard-serverless instance:

To send messages to events topic

```shell
❯ oc run eda-serverless-kafka-producer -ti \
 --image=registry.redhat.io/amq7/amq-streams-kafka-28-rhel8:1.8.0 \
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
kn service update backend-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/backend:2.3.0-SNAPSHOT
```

To update `data-streaming-serverless` service:

```shell
kn service update data-streaming-serverless --image image-registry.openshift-image-registry.svc:5000/eda-workshop/data-streaming:2.3.0-SNAPSHOT
```

## Main References

[Installing Knative Serving](https://docs.openshift.com/container-platform/4.7/serverless/admin_guide/installing-knative-serving.html)
[Installing Knative Eventing](https://docs.openshift.com/container-platform/4.7/serverless/admin_guide/installing-knative-eventing.html)
