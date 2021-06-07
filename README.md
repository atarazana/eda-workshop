# Event-Driven Architecture Workshop

## Prepare OpenShift

This demo has been tested in Red Hat OpenShift Container Platform 4.7 version.

As a normal user in your OpenShift cluster, create a ```eda-workshop``` namespace:

```shell script
❯ oc login -u user
❯ oc new-project eda-workshop
```

## Deploy Operators

Follow [the instructions](./01-operators/README.md)

## Deploy Metrics Platform

Follow [the instructions](./02-metrics/README.md)

## Deploy Databases

Follow [the instructions](./03-databases/README.md)

## Deploy Streaming Platform

Follow [the instructions](./04-kafka/README.md)

## Deploy Service Registry

Follow [the instructions](./05-service-registry/README.md)

## Deploy KafkaConnect

Follow [the instructions](./06-kafka-connect/README.md)

## Deploy DataGrid

Follow [the instructions](./07-datagrid/README.md)

## Deploy Quarkus Business Application

Follow [the instructions](./08-quarkus-business-app/README.md)

## Publish Schemas API

Follow [the instructions](./09-event-schemas-api/README.md)

## Deploy Quarkus Streaming Application

Follow [the instructions](./10-quarkus-streaming/README.md)

## Deploy Quarkus Backend Application

Follow [the instructions](./11-quarkus-backend/README.md)

## Deploy Quarkus Dashboard Application

Follow [the instructions](./12-quarkus-dashboard/README.md)

## Deploy Serverless Services

Follow [the instructions](./14-serverless/README.md)
