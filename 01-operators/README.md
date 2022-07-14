# Operators

Operators are installed using Operator Lifecycle Manager (OLM), tool to help manage the
Operators running on a cluster.

**NOTE:** To deploy the different Operators we need to use an user with ```cluster-admin``` role.

```shell
oc login -u admin-user
```

## Global Operators

Install the global (or cluster-wide) operators running the following command:

```shell
oc apply -f cluster-wide/
```

These operators will be installed in the ```openshift-operators``` namespace and will be usable
from any namespaces in the cluster.

```shell
❯ oc get pod -n openshift-operators
NAME                                                      READY   STATUS    RESTARTS   AGE
amq-streams-cluster-operator-v2.1.0-4-7f66787968-jhc55    1/1     Running   0          6h8m
apicurio-registry-operator-55bb56dd6-5rdgc                1/1     Running   0          6h7m
infinispan-operator-controller-manager-5d9d4ddf76-kk4sb   1/1     Running   0          6h7m
knative-openshift-57fc57698b-c8vmj                        1/1     Running   0          6h7m
knative-openshift-ingress-5589fb9d7-sblb4                 1/1     Running   0          6h7m
knative-operator-957c8745-bl8t9                           1/1     Running   0          6h7m
```

To check the subscription statuses:

```shell
❯ oc get csv
amqstreams.v2.1.0-4                               Red Hat Integration - AMQ Streams                 2.1.0-4                amqstreams.v2.1.0-3                               Succeeded
datagrid-operator.v8.3.6                          Data Grid                                         8.3.6                  datagrid-operator.v8.3.4                          Succeeded
serverless-operator.v1.23.0                       Red Hat OpenShift Serverless                      1.23.0                 serverless-operator.v1.22.0                       Succeeded
service-registry-operator.v2.0.5-0.1655396591.p   Red Hat Integration - Service Registry Operator   2.0.5+0.1655396591.p   service-registry-operator.v2.0.5-0.1652887585.p   Succeeded
```

## Local Operators

Install the local (or namespace-wide) operators running the following command:

```shell
oc create -f namespace-wide/
```

These operators will be installed in the ```eda-workshop``` namespace and will be usable
from only that namespace.

```shell
❯ oc get pod -n eda-workshop
NAME                                                   READY   STATUS    RESTARTS      AGE
grafana-operator-controller-manager-59ccc6df4d-qq94l   2/2     Running   0             24m
prometheus-operator-85f4f9fddc-nwjdv                   1/1     Running   0             24m
prometheus-prometheus-0                                2/2     Running   1 (23m ago)   24m
```

To check the subscription statuses:

```shell
❯ oc get csv
NAME                               DISPLAY                VERSION   REPLACES                      PHASE
grafana-operator.v4.4.1            Grafana Operator       4.4.1     grafana-operator.v4.4.0       Succeeded
prometheusoperator.0.56.3          Prometheus Operator    0.56.3    prometheusoperator.0.47.0     Succeeded
```
