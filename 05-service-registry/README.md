# Deploy Service Registry

To deploy Service Registry as API Schema platform execute:

```shell
oc apply -f apicurio-registry.yaml
```

To check the current status of the Service Registry:

```shell
❯ oc get apicurioregistry eda-registry -o yaml
# ...
spec:
  configuration:
    kafkasql:
      bootstrapServers: event-bus-kafka-bootstrap:9092
    logLevel: INFO
    persistence: kafkasql
    ui:
      readOnly: false
  deployment:
    host: eda-registry.eda-workshop.apps.labs.sandbox.opentlc.com
    replicas: 1
status:
  conditions:
  - lastTransitionTime: "2021-05-21T09:30:19Z"
    message: ""
    reason: Reconciled
    status: "True"
    type: Ready
  info:
    host: eda-registry.eda-workshop.apps.labs.sandbox.opentlc.com
  managedResources:
  - kind: Deployment
    name: eda-registry-deployment
    namespace: eda-workshop
  - kind: Service
    name: eda-registry-service
    namespace: eda-workshop
  - kind: Ingress
    name: eda-registry-ingress
    namespace: eda-workshop
```

A new pod will be deployed:

```shell
❯ oc get pod | grep registry
NAME                                       READY   STATUS    RESTARTS   AGE
eda-registry-deployment-67c7895564-cbfvf   1/1     Running   0          10m
```

Service Registry is available outside OpenShift cluster with the next route over
the `eda-registry` application:

```shell
oc get route -l app=eda-registry -o jsonpath='{.items[0].spec.host}'
```
