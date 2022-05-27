# Deploy Red Hat DataGrid

To deploy Red Hat DataGrid execute:

```shell
oc apply -f secrets/
oc apply -f eda-infinispan.yaml
```

Secrets contain a set of credentials of the following users:

* **developer**: User to allow put and get data from the different caches
* **operator**: User to allow the DataGrid operator manages the lifecycle of caches

To check the current status of the DataGrid:

```shell
❯ oc get infinispan eda-infinispan -o yaml
# ...
status:
  conditions:
  - status: "True"
    type: PreliminaryChecksPassed
  - message: 'View: eda-infinispan-0-6324,eda-infinispan-1-13135,eda-infinispan-2-45969'
    status: "True"
    type: WellFormed
  consoleUrl: http://eda-infinispan-external-eda-workshop.apps.sandbox.opentlc.com/console
  podStatus:
    ready:
    - eda-infinispan-1
    - eda-infinispan-2
    - eda-infinispan-3
  statefulSetName: eda-infinispan
```

A set of new pods will be deployed:

```shell
❯ oc get pod | grep infinispan
NAME                                               READY   STATUS    RESTARTS   AGE
eda-infinispan-0                                   1/1     Running      0              10m
eda-infinispan-1                                   1/1     Running      0              9m26s
eda-infinispan-2                                   1/1     Running      0              8m44s
eda-infinispan-config-listener-85b985c588-86szv    1/1     Running      0              8m2s
```

To get the route to access DataGrid Administration Console:

```shell
oc get route eda-infinispan-external -o jsonpath='{.spec.host}'
```

## Deploying Data Caches

This architecture requires a set of Data Caches to store some important
information.

To deploy Data Caches:

```shell
oc apply -f caches/
```

We could test and verify the Caches:

```shell
❯ oc get cache
NAME                               AGE
eda-infinispan-aggregate-metrics   7s
eda-infinispan-alerts              7s
eda-infinispan-clients             7s
eda-infinispan-regions             7s
```

To get the current status of a Data Cache:

```shell
❯ oc get cache eda-infinispan-clients -o yaml
apiVersion: infinispan.org/v2alpha1
kind: Cache
metadata:
  name: eda-infinispan-clients
# ...
spec:
  # ...
  clusterName: eda-infinispan
  name: clients
status:
  conditions:
  - status: "True"
    type: Ready
  serviceName: eda-infinispan
```
