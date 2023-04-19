#!/bin/bash

source 00-ocp-utils.sh

#echo "Regions Table Status (Before Updating)"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "SELECT* FROM regions;"

#echo "Updating Region Table"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'Spain' WHERE code = 'es';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'Portugal' WHERE code = 'pt';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'United Kingdom' WHERE code = 'uk';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'Italy' WHERE code = 'it';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'France' WHERE code = 'fr';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'Germany' WHERE code = 'ge';"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "UPDATE regions SET description = 'United States of America' WHERE code = 'us';"

#echo "Regions Table Status (After Updating)"
#oc exec -it $(oc get pods --selector deployment=mysql-enterprise -o jsonpath='{.items[0].metadata.name}') -- mysql -u mysqluser -pmysqlpw enterprise -e "SELECT* FROM regions;"

echo "**********************************"
echo "Registering API schemas"
echo "**********************************"

cd 09-event-schemas-api
./mvnw generate-sources -Papi-schemas-register \
  -Dapicurio.registry.url=http://$(oc get route -l app=eda-registry -o jsonpath='{.items[0].spec.host}')/apis/registry/v2

echo "**********************************"
echo "Deploying Business Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-17~https://github.com/atarazana/eda-workshop.git \
    --context-dir=08-quarkus-business-app --name business-app

echo "**********************************"
echo "Deploying Data Streaming Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-17~https://github.com/atarazana/eda-workshop.git \
    --context-dir=10-quarkus-streaming --name data-streaming

echo "**********************************"
echo "Deploying Backend Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-17~https://github.com/atarazana/eda-workshop.git \
    --context-dir=11-quarkus-backend --name backend

echo "**********************************"
echo "Deploying Dashboard Application"
echo "**********************************"

oc new-app registry.access.redhat.com/ubi8/openjdk-17~https://github.com/atarazana/eda-workshop.git \
    --context-dir=12-quarkus-dashboard --name dashboard
oc expose svc/dashboard

check_build_completed "business-app" "eda-workshop"
check_build_completed "data-streaming" "eda-workshop"
check_build_completed "backend" "eda-workshop"
check_build_completed "dashboard" "eda-workshop"

check_deployment_ready "business-app" "eda-workshop"
check_deployment_ready "data-streaming" "eda-workshop"
check_deployment_ready "backend" "eda-workshop"
check_deployment_ready "dashboard" "eda-workshop"

echo "**********************************"
echo "Tagging Application Images"
echo "**********************************"

oc tag business-app:latest business-app:2.10.1-SNAPSHOT
oc tag data-streaming:latest data-streaming:2.10.1-SNAPSHOT
oc tag backend:latest backend:2.10.1-SNAPSHOT
oc tag dashboard:latest dashboard:2.10.1-SNAPSHOT
