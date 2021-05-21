# Deploy Red Hat DataGrid

To deploy Red Hat DataGrid execute:

```shell script
oc apply -f datagrid.yaml
```

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
