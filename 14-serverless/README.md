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
NAME                                                    READY   STATUS      RESTARTS   AGE
activator-67dcc8667f-pqdqm                              2/2     Running     0          2m4s
activator-67dcc8667f-stbwz                              2/2     Running     0          2m4s
autoscaler-5746cc94c7-7ktxn                             2/2     Running     0          2m4s
autoscaler-5746cc94c7-kknnt                             2/2     Running     0          2m4s
autoscaler-hpa-5668d4cbd9-jqtlz                         2/2     Running     0          2m1s
autoscaler-hpa-5668d4cbd9-p2wc4                         2/2     Running     0          2m1s
controller-7f4dff8dbf-8l8hl                             2/2     Running     0          95s
controller-7f4dff8dbf-mf8r8                             2/2     Running     0          114s
domain-mapping-55cd8bf549-582nj                         2/2     Running     0          2m3s
domain-mapping-55cd8bf549-sr2s2                         2/2     Running     0          2m3s
domainmapping-webhook-688cc48f5c-g2ltk                  2/2     Running     0          2m3s
domainmapping-webhook-688cc48f5c-t6bc6                  2/2     Running     0          2m3s
storage-version-migration-serving-serving-1.1.2-xpt7d   0/1     Completed   0          2m1s
webhook-6644cfd46b-dzkr2                                2/2     Running     0          2m2s
webhook-6644cfd46b-qggtr                                2/2     Running     0          2m2s
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
NAME                                                      READY   STATUS      RESTARTS   AGE
eventing-controller-7d8698996f-chbbh                      2/2     Running     0          64s
eventing-controller-7d8698996f-tlhwz                      2/2     Running     0          64s
eventing-webhook-5bb687ffc6-485gt                         2/2     Running     0          64s
eventing-webhook-5bb687ffc6-6kmsj                         2/2     Running     0          64s
imc-controller-79c8f69cf5-69zbs                           2/2     Running     0          60s
imc-controller-79c8f69cf5-n9hmb                           2/2     Running     0          60s
imc-dispatcher-6b8b49794d-84fzd                           2/2     Running     0          59s
imc-dispatcher-6b8b49794d-ssptj                           2/2     Running     0          59s
kafka-ch-controller-78fb88fddc-p5p4z                      2/2     Running     0          80s
kafka-controller-995bb5d97-6kw8p                          2/2     Running     0          77s
kafka-controller-post-install-1.22.0-f9gmz                0/1     Completed   0          79s
kafka-source-dispatcher-6cc878c59c-bkns7                  2/2     Running     0          79s
kafka-webhook-855968c6f-vqrw9                             2/2     Running     0          80s
kafka-webhook-eventing-558cfdc7b6-4tbjr                   2/2     Running     0          79s
knative-kafka-storage-version-migrator-1.22.0-qkv2w       0/1     Completed   0          79s
mt-broker-controller-bd467886b-852ns                      2/2     Running     0          57s
mt-broker-controller-bd467886b-b7qvj                      2/2     Running     0          57s
mt-broker-filter-6b944bf9bb-9ln7m                         2/2     Running     0          58s
mt-broker-filter-6b944bf9bb-cqrw8                         2/2     Running     0          58s
mt-broker-ingress-7b446c5678-r2lhh                        2/2     Running     0          58s
mt-broker-ingress-7b446c5678-w7l8z                        2/2     Running     0          58s
storage-version-migration-eventing-eventing-1.1.0-bqqrs   0/1     Completed   0          57s
storage-version-migration-kafka-channel-1.22.0-4ffbm      0/1     Completed   0          79s
sugar-controller-547b949d59-dvwl5                         2/2     Running     0          57s
sugar-controller-547b949d59-sfrnp                         2/2     Running     0          57s
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
