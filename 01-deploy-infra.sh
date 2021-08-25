#!/bin/bash

echo "**********************************"
echo "Creating EDA Workshop Namespace"
echo "**********************************"

oc new-project eda-workshop

echo "**********************************"
echo "Deploying Cluster Wide Operators"
echo "**********************************"

oc apply -f 01-operators/cluster-wide/

# TODO Add checks to move to next step
sleep 120

echo "**********************************"
echo "Deploying Namespace Wide Operators"
echo "**********************************"

oc apply -f 01-operators/namespace-wide/

# TODO Add checks to move to next step
sleep 180

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

# TODO Add checks to move to next step
sleep 180

echo "**********************************"
echo "Deploying Grafana"
echo "**********************************"

oc apply -f 02-metrics/grafana/grafana.yaml
oc apply -f 02-metrics/grafana/dashboards/

# TODO Add checks to move to next step
sleep 180

echo "**********************************"
echo "Deploying Databases"
echo "**********************************"

echo "**********************************"
echo "Deploying Enterprise Database"
echo "**********************************"

oc new-build ./03-databases/mysql/enterprise --name=mysql-enterprise
sleep 30

oc start-build mysql-enterprise --from-dir=./03-databases/mysql/enterprise --follow
oc new-app mysql-enterprise:latest -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw
oc label deployment mysql-enterprise app.kubernetes.io/part-of=mysql-databases

echo "**********************************"
echo "Deploying Inventory Database"
echo "**********************************"

oc new-build ./03-databases/mysql/inventory --name=mysql-inventory
sleep 30

oc start-build mysql-inventory --from-dir=./03-databases/mysql/inventory --follow
oc new-app mysql-inventory:latest -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw
oc label deployment mysql-inventory app.kubernetes.io/part-of=mysql-databases

echo "**********************************"
echo "Deploying Event Bus - Apache Kafka"
echo "**********************************"

oc apply -f 04-kafka/kafka/configmap/
oc apply -f 04-kafka/kafka/event-bus-kafka.yml

# TODO Add checks to move to next step
sleep 600

#oc extract secret/event-bus-cluster-ca-cert --keys=ca.crt --to=- > ca.crt
#keytool -import -trustcacerts -alias root -file ca.crt -keystore truststore.jks -storepass password -noprompt

echo "**********************************"
echo "Deploying Kafka Topics"
echo "**********************************"

oc apply -f 04-kafka/topics/data
oc apply -f 04-kafka/topics/domain
oc apply -f 04-kafka/topics/events

# TODO Add checks to move to next step
sleep 60

echo "**********************************"
echo "Deploying Apicurio Service Registry"
echo "**********************************"

oc apply -f 05-service-registry/apicurio-registry.yaml

# TODO Add checks to move to next step
sleep 180

echo "**********************************"
echo "Deploying Kafka Connect Cluster"
echo "**********************************"

oc apply -f 06-kafka-connect/eda-kafka-connect-is.yaml
oc apply -f 06-kafka-connect/topics/
oc apply -f 06-kafka-connect/configmap/
oc apply -f 06-kafka-connect/eda-kafka-connect.yaml

# TODO Add checks to move to next step
sleep 300

echo "**********************************"
echo "Deploying Kafka Connectors"
echo "**********************************"

oc apply -f 06-kafka-connect/connectors/
oc apply -f 06-kafka-connect/debezium-mysql/

echo "**********************************"
echo "Deploying DataGrid"
echo "**********************************"

oc apply -f 07-datagrid/secrets/
oc apply -f 07-datagrid/eda-infinispan.yaml

# TODO Add checks to move to next step
sleep 300

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

# TODO Add checks to move to next step
sleep 180

echo "**********************************"
echo "Deploying Serverless Eventing"
echo "**********************************"

oc apply -f 14-serverless/knative-eventing/knative-eventing.yaml
oc apply -f 14-serverless/knative-eventing/knative-kafka.yaml -n knative-eventing
