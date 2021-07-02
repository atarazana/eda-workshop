#!/bin/bash

echo "**********************************"
echo "Deploying Serverless Services"
echo "**********************************"

oc scale deployment/data-streaming --replicas=0
oc scale deployment/backend --replicas=0
oc scale deployment/dashboard --replicas=0

echo "**********************************"
echo "Deploying Serverless Application Services"
echo "**********************************"

oc apply -f 14-serverless/knative-eventing/topics
oc apply -f 14-serverless/service/
oc apply -f 14-serverless/knative-eventing/kafka-source
