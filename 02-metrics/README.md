# Deploy Prometheus and Grafana Operators

Red Hat AMQ Streams supports Prometheus metrics using Prometheus JMX exporter to convert the JMX metrics supported
by Apache Kafka and Apache Zookeeper to Prometheus metrics. This feature helps us to monitor the cluster
easily using Prometheus to store the metrics and Grafana Dashboards to expose them.

This repo will use [Prometheus Operator](https://operatorhub.io/operator/prometheus) and
[Grafana Operator](https://operatorhub.io/operator/grafana-operator) to deploy them and monitor the
Apache Kafka ecosystem easily.

To get detailed configuration, please, review the
[Introducing Metrics to Kafka](https://strimzi.io/docs/operators/latest/deploying.html#assembly-metrics-str)
from Strimzi Documentation site.

**NOTE:** To deploy Prometheus and Grafana Operators we need to install previously an `OperatorGroup` to match the
Operator's installation mode and the namespace. This step is describe in
[Adding Operators to a cluster](https://docs.openshift.com/container-platform/4.7/operators/admin/olm-adding-operators-to-cluster.html)
page from [OpenShift Documentation](https://docs.openshift.com/container-platform/4.7/welcome/index.html) site.

As soon the operators are installed we could follow to install the Metrics Platform and the Apache Kafka Cluster.

## Deploy Metrics Platform

Prometheus and Grafana Operator help us to deploy our local Prometheus Server and Grafana instance where
we could manage the metrics from our Apache Kafka Cluster.

To deploy a local Prometheus Server in ```eda-workshop``` namespace.

```shell script
❯ oc apply -f prometheus/prometheus-additional.yaml
❯ oc apply -f prometheus/strimzi-pod-monitor.yaml
❯ oc apply -f prometheus/prometheus-rules.yaml
❯ oc apply -f prometheus/prometheus.yaml
```

A Prometheus instance will be available with a service and a route:

```shell script
❯ oc get svc
NAME                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
prometheus-operated   ClusterIP   None             <none>        9090/TCP   34s
❯ oc get route
NAME        HOST/PORT                                                  PATH   SERVICES             PORT   TERMINATION   WILDCARD
prometheus  prometheus-eda-workshop.apps.labs.sandbox1862.opentlc.com         prometheus-operated  web                  None
```

To get the route to access Prometheus:

```shell script
oc get route prometheus -o jsonpath='{.spec.host}'
```

Deploy Grafana:

```shell script
oc apply -f grafana/grafana.yaml
```

Grafana will deploy a Data Source connected to the Prometheus server available by Prometheus
in the endpoint ```http://prometheus-operated:9090```. The Grafana Server will use that Data Source
to get the metrics.

Grafana has a set of dashboards to review the metrics from Apache Zookeeper and Apache Kafka Cluster. We could
deploy it as:

```shell script
oc apply -f grafana/dashboards/
```

To get the route to access Grafana:

```shell script
oc get route grafana-route -o jsonpath='{.spec.host}'
```

Use the original credentials **root/secret** as user/password. These credentials are defined in the
[Grafana CR](./metrics/grafana/grafana.yaml) file.
