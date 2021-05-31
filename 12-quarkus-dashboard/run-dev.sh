#!/bin/sh
CLUSTER_NAME=event-bus
PROJECT_NAME=eda-workshop

export KAFKA_SERVICE_HOST=$(oc -n ${PROJECT_NAME} get routes ${CLUSTER_NAME}-kafka-bootstrap -o=jsonpath='{.status.ingress[0].host}{"\n"}')
mvn quarkus:dev