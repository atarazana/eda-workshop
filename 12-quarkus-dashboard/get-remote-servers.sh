#!/bin/sh
export CLUSTER_NAME=event-bus
export PROJECT_NAME=eda-workshop

export KAFKA_SERVICE_HOST=$(oc -n ${PROJECT_NAME} get routes ${CLUSTER_NAME}-kafka-bootstrap -o=jsonpath='{.status.ingress[0].host}{"\n"}')
export INFINISPAN_SERVICE_HOST=$(oc -n ${PROJECT_NAME} get routes/eda-infinispan-external -o=jsonpath='{.status.ingress[0].host}{"\n"}')
#mvn quarkus:dev
