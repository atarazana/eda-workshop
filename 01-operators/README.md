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
NAME                                                         READY      STATUS       RESTARTS      AGE
amq-streams-cluster-operator-v2.3.0-3-f4fc7f8bd-n8k7s        1/1     Running   0          18h
apicurio-registry-operator-v1.1.0-redhat.1-c7d78cf88-zvhkd   1/1     Running   0          21h
infinispan-operator-controller-manager-99b46985-vg2l9        1/1     Running   0          20h
knative-openshift-7955656555-tfpcw                           1/1     Running   0          20h
knative-openshift-ingress-68c4847689-jdq4b                   1/1     Running   0          21h
knative-operator-webhook-68db7f68cf-ss9rs                    1/1     Running   0          20h
```

To check the subscription statuses:

```shell
❯ oc get csv
NAME                               DISPLAY                                           VERSION   REPLACES                           PHASE
amqstreams.v2.3.0-3                AMQ Streams                                       2.3.0-3   amqstreams.v2.3.0-2                Succeeded
datagrid-operator.v8.3.9           Data Grid                                         8.3.9     datagrid-operator.v8.3.6           Succeeded
serverless-operator.v1.28.0        Red Hat OpenShift Serverless                      1.28.0    serverless-operator.v1.27.1        Succeeded
service-registry-operator.v2.1.4   Red Hat Integration - Service Registry Operator   2.1.4     service-registry-operator.v2.1.3   Succeeded
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
NAME                                                   READY   STATUS    RESTARTS   AGE
grafana-operator-controller-manager-64457bf95b-bfnj2   2/2     Running   0          3h46m
prometheus-operator-68c545f897-mgpc2                   1/1     Running   0          3h50m
prometheus-prometheus-0                                2/2     Running   0          3h48m
```

To check the subscription statuses:

```shell
❯ oc get csv
NAME                               DISPLAY                 VERSION   REPLACES                  PHASE
grafana-operator.v4.10.0           Grafana Operator        4.10.0    grafana-operator.v4.9.0   Succeeded
rhods-prometheus-operator.4.10.0   Prometheus Operator     4.10.0                              Succeeded
```
