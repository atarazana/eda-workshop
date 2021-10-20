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
NAME                                                  READY   STATUS    RESTARTS   AGE
amq-streams-cluster-operator-v1.8.0-75f8c6f46c-s7mcb  1/1     Running   0          25h
apicurio-registry-operator-576d45bbd9-jqdcb           1/1     Running   0          2m36s
infinispan-operator-5c77fcfbc9-wg4ws                  1/1     Running   0          2m40s
knative-openshift-9d6b46dcf-2dfdz                     1/1     Running   0          27h
knative-openshift-ingress-cbfb59bc7-qk56g             1/1     Running   0          27h
knative-operator-55b5877c97-m4gtn                     1/1     Running   0          27h
```

To check the subscription statuses:

```shell
❯ oc get csv
NAME                               DISPLAY                                           VERSION   REPLACES                      PHASE
amqstreams.v1.8.0                  Red Hat Integration - AMQ Streams                 1.8.0     amqstreams.v1.7.3                  Succeeded
datagrid-operator.v8.2.3           Data Grid                                         8.2.3     datagrid-operator.v8.2.2           Succeeded
serverless-operator.v1.16.0        Red Hat OpenShift Serverless                      1.16.0    serverless-operator.v1.15.0        Succeeded
service-registry-operator.v2.0.1   Red Hat Integration - Service Registry Operator   2.0.1     service-registry-operator.v2.0.0   Succeeded
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
NAME                                         READY   STATUS             RESTARTS   AGE
grafana-operator-5ccd457bc6-rxs6t            1/1     Running            0          28h
prometheus-operator-7d874db8c-rpfmm          1/1     Running            0          28h
```

To check the subscription statuses:

```shell
❯ oc get csv
NAME                               DISPLAY                VERSION   REPLACES                      PHASE
grafana-operator.v3.10.2           Grafana Operator       3.10.2    grafana-operator.v3.10.1      Succeeded
prometheusoperator.0.47.0          Prometheus Operator    0.47.0    prometheusoperator.0.37.0     Succeeded
```
