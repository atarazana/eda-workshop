# Deploy Red Hat DataGrid

To deploy Red Hat DataGrid execute:

```shell script
oc apply -f secrets/
oc apply -f eda-infinispan.yaml
```

Secrets contain a set of credentials of the following users:

* **developer**: User to allow put and get data from the different caches
* **operator**: User to allow the DataGrid operator manages the lifecycle of caches

To check the current status of the DataGrid:

```shell script
❯ oc get infinispan eda-infinispan -o yaml
...
status:
  conditions:
  - status: "True"
    type: PreliminaryChecksPassed
  - message: 'View: eda-infinispan-0-3647,eda-infinispan-1-987,eda-infinispan-2-13866'
    status: "True"
    type: WellFormed
  statefulSetName: eda-infinispan
```

A set of new pods will be deployed:

```shell script
❯ oc get pod | grep infinispan
NAME               READY   STATUS    RESTARTS   AGE
eda-infinispan-0   1/1     Running   0          10m
eda-infinispan-1   1/1     Running   0          9m5s
eda-infinispan-2   1/1     Running   0          7m58s
```

## Deploying Data Caches

This architecture requires a set of Data Caches to store some important
information.

To deploy Data Caches:

```shell script
oc apply -f caches/
```

We could test and verify the Caches:

```shell script
❯ oc get cache
NAME                     AGE
eda-infinispan-clients   10s
eda-infinispan-regions   10s
```

To get the current status of a Data Cache:

```shell script
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
