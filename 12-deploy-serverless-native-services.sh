#!/bin/bash

echo "**********************************"
echo "Deploying Serverless Native"
echo "**********************************"

oc delete kafkasource data-streaming-kafka-source
oc delete kafkasource backend-kafka-source

oc apply -f 15-native-services/service/
oc apply -f 15-native-services/knative-eventing/kafka-source/
