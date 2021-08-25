#!/bin/bash

echo "**********************************"
echo "Deploying Business Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-11~https://github.com/atarazana/eda-workshop.git \
    --context-dir=08-quarkus-business-app --name business-app

echo "**********************************"
echo "Registering API schemas"
echo "**********************************"

cd 09-event-schemas-api
./mvnw generate-sources -Papi-schemas-register \
  -Dapicurio.registry.url=http://$(oc get route -l app=eda-registry -o jsonpath='{.items[0].spec.host}')/apis/registry/v2

echo "**********************************"
echo "Deploying Data Streaming Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-11~https://github.com/atarazana/eda-workshop.git \
    --context-dir=10-quarkus-streaming --name data-streaming

echo "**********************************"
echo "Deploying Backend Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-11~https://github.com/atarazana/eda-workshop.git \
    --context-dir=11-quarkus-backend --name backend

echo "**********************************"
echo "Deploying Dashboard Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-11~https://github.com/atarazana/eda-workshop.git \
    --context-dir=12-quarkus-dashboard --name dashboard
oc expose svc/dashboard

sleep 420

echo "**********************************"
echo "Tagging Application Images"
echo "**********************************"

oc tag business-app:latest business-app:2.1.3-SNAPSHOT
oc tag data-streaming:latest data-streaming:2.1.3-SNAPSHOT
oc tag backend:latest backend:2.1.3-SNAPSHOT
oc tag dashboard:latest dashboard:2.1.3-SNAPSHOT
