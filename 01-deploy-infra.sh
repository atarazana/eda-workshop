#!/bin/bash

source 00-ocp-utils.sh

echo "**********************************"
echo "Creating EDA Workshop Namespace"
echo "**********************************"

create_namespace "eda-workshop"

echo "**********************************"
echo "Deploying Cluster Wide Operators"
echo "**********************************"

oc apply -f 01-operators/cluster-wide/

check_operator_ready "AMQ Streams" "eda-workshop"
check_operator_ready "Data Grid" "eda-workshop"
check_operator_ready "Red Hat OpenShift Serverless" "eda-workshop"
check_operator_ready "Red Hat Integration - Service Registry Operator" "eda-workshop"

echo "**********************************"
echo "Deploying Namespace Wide Operators"
echo "**********************************"

oc apply -f 01-operators/namespace-wide/

check_operator_ready "Prometheus Operator" "eda-workshop"
check_operator_ready "Grafana Operator" "eda-workshop"

echo "**********************************"
echo "Deploying Monitoring Platform"
echo "**********************************"

echo "**********************************"
echo "Deploying Prometheus"
echo "**********************************"

oc apply -f 02-metrics/prometheus/prometheus-additional.yaml
oc apply -f 02-metrics/prometheus/strimzi-pod-monitor.yaml
oc apply -f 02-metrics/prometheus/prometheus-rules.yaml
oc apply -f 02-metrics/prometheus/prometheus.yaml

echo "**********************************"
echo "Deploying Grafana"
echo "**********************************"

oc apply -f 02-metrics/grafana/grafana.yaml
oc apply -f 02-metrics/grafana/dashboards/

check_statefulset_ready "prometheus-prometheus" "eda-workshop"
check_deployment_ready "grafana-deployment" "eda-workshop"

echo "**********************************"
echo "Deploying Databases"
echo "**********************************"

echo "**********************************"
echo "Deploying Enterprise Database"
echo "**********************************"

create_build_config "mysql-enterprise" "./03-databases/mysql/enterprise" "eda-workshop"
check_build_completed "mysql-enterprise" "eda-workshop"

create_new_app "mysql-enterprise" "mysql-enterprise:latest" "eda-workshop" "-e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw" "app.kubernetes.io/part-of=mysql-databases"

echo "**********************************"
echo "Deploying Inventory Database"
echo "**********************************"

create_build_config "mysql-inventory" "./03-databases/mysql/inventory" "eda-workshop"
check_build_completed "mysql-inventory" "eda-workshop"

create_new_app "mysql-inventory" "mysql-inventory:latest" "eda-workshop" "-e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw" "app.kubernetes.io/part-of=mysql-databases"

echo "**********************************"
echo "Deploying Event Bus - Apache Kafka"
echo "**********************************"

oc apply -f 04-kafka/kafka/configmap/
oc apply -f 04-kafka/kafka/event-bus-kafka.yml

check_kafka_deployed "event-bus" "eda-workshop"

#oc extract secret/event-bus-cluster-ca-cert --keys=ca.crt --to=- > ca.crt
#keytool -import -trustcacerts -alias root -file ca.crt -keystore truststore.jks -storepass password -noprompt

echo "**********************************"
echo "Deploying Kafka Topics"
echo "**********************************"

oc apply -f 04-kafka/topics/data
oc apply -f 04-kafka/topics/domain
oc apply -f 04-kafka/topics/events

echo "**********************************"
echo "Deploying Apicurio Service Registry"
echo "**********************************"

oc apply -f 05-service-registry/apicurio-registry.yaml

check_deployment_ready "eda-registry-deployment" "eda-workshop"

echo "**********************************"
echo "Deploying Kafka Connect Cluster"
echo "**********************************"

oc apply -f 06-kafka-connect/eda-kafka-connect-is.yaml
oc apply -f 06-kafka-connect/topics/
oc apply -f 06-kafka-connect/configmap/
oc apply -f 06-kafka-connect/eda-kafka-connect.yaml

check_kafkaconnect_deployed "eda-kafka-connect" "eda-workshop"

echo "**********************************"
echo "Deploying Kafka Connectors"
echo "**********************************"

#oc apply -f 06-kafka-connect/connectors/
oc apply -f 06-kafka-connect/debezium-mysql/

echo "**********************************"
echo "Deploying DataGrid"
echo "**********************************"

oc apply -f 07-datagrid/secrets/
oc apply -f 07-datagrid/eda-infinispan.yaml

check_statefulset_ready "eda-infinispan" "eda-workshop"

echo "**********************************"
echo "Deploying DataGrid Cache"
echo "**********************************"

oc apply -f 07-datagrid/caches/

echo "**********************************"
echo "Deploying Serverless Services"
echo "**********************************"

echo "**********************************"
echo "Deploying Serverless Serving"
echo "**********************************"

oc apply -f 14-serverless/knative-serving/knative-serving.yaml

check_knative_serving_ready "knative-serving"

echo "**********************************"
echo "Deploying Serverless Eventing"
echo "**********************************"

oc apply -f 14-serverless/knative-eventing/knative-eventing.yaml
oc apply -f 14-serverless/knative-eventing/knative-kafka.yaml -n knative-eventing

check_knative_eventing_ready "knative-eventing"
