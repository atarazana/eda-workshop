# Event-Driven Architecture Workshop

![License](https://img.shields.io/github/license/atarazana/eda-workshop?style=plastic)
![Main Lang](https://img.shields.io/github/languages/top/atarazana/eda-workshop)
![Languages](https://img.shields.io/github/languages/count/atarazana/eda-workshop)

![Business App Build Status](https://github.com/atarazana/eda-workshop/actions/workflows/quarkus-business-app.maven.yml/badge.svg?branch=main)
![Event Schemas API Build Status](https://github.com/atarazana/eda-workshop/actions/workflows/event-schemas-api.maven.yml/badge.svg?branch=main)
![Quarkus Streaming Build Status](https://github.com/atarazana/eda-workshop/actions/workflows/quarkus-streaming.maven.yml/badge.svg?branch=main)
![Quarkus Backend Build Status](https://github.com/atarazana/eda-workshop/actions/workflows/quarkus-backend.maven.yml/badge.svg?branch=main)
![Quarkus Dashboard Build Status](https://github.com/atarazana/eda-workshop/actions/workflows/quarkus-dashboard.maven.yml/badge.svg?branch=main)

![Last Commit](https://img.shields.io/github/last-commit/atarazana/eda-workshop)
![Tag](https://img.shields.io/github/v/tag/atarazana/eda-workshop)
![Downloads](https://img.shields.io/github/downloads/atarazana/eda-workshop/total)

In the digital transformation strategies of companies, Serverless technologies, Event-Driven Architectures (EDA)
and data capture patterns (Change Data Capture) are gaining more strength every day.
 
Within this scenario, [Red Hat OpenShift Serverless](https://www.openshift.com/learn/topics/serverless),
[Red Hat AMQ Streams](https://www.redhat.com/en/technologies/jboss-middleware/amq),
[Red Hat Integration](https://www.redhat.com/en/products/integration),
[Red Hat Data Grid](https://www.redhat.com/en/technologies/jboss-middleware/data-grid),
and [Red Hat build of Quarkus](https://access.redhat.com/products/quarkus), offer companies
significant cost savings and multiple operational benefits by shifting infrastructure and
provisioning responsibilities to the different frameworks. This enables you
to solve problems quickly, at scale, and with high availability.

This repository includes a practical business example so that we can adopt modern and agile application
designs and implementations (containers, Serverless, streaming, etc.).

The logical architecture diagram of this use case is:

![Event-Driven Logical Architecture](./img/eda-logical-architecture.png "Event-Driven Logical Architecture")

At the end of the instructions you will have deployed a full Event-Driven Architecture with the
following deployment topology:

![Event-Driven Physical Architecture](./img/eda-physical-architecture.png "Event-Driven Physical Architecture in OpenShift")

This file include the list of steps to deploy easily this use case for your learning and testing efforts
about these amazing technologies and products.

**HINT**: If you want to deploy easily, we created a set of shell scripts with all the main
commands to deploy the infrastructure, applications and serverless services. Check the shell scripts in
the root of this repository.

## Prepare OpenShift

This architecture has been tested in Red Hat OpenShift Container Platform 4.12 version and the following
operators:

* Red Hat Integration - AMQ Streams 2.3.0 (Apache Kafka 3.3)
* Red Hat Integration - Service Registry 2.1.4
* Red Hat Data Grid 8.3.9
* Red Hat OpenShift Serverless 1.28.0
* Prometheus 4.10.0
* Grafana 4.10.0

As a normal user in your OpenShift cluster, create a ```eda-workshop``` namespace:

```shell
❯ oc login -u user
❯ oc new-project eda-workshop
```

This workshop requires deploy many different components and
it is needed to have enough resources. This workshop was
tested in "OpenShift 4.12 Workshop" Service provided
in the [Red Hat Product Demo Platform](https://demo.redhat.com/).

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

## Deploy Native Serverless Services

Follow [the instructions](./15-native-services/README.md)

## Bonus Track - Scripted deployment

There is a set of different shell scripts that allow to deploy easily all the
components of this repository avoiding a step-by-step process. The following
scripts can help you to deploy this solution in your cluster in few minutes:

* [`01-deploy-infra.sh`](./01-deploy-infra.sh) - Deploy the infrastructure of
the solution: Operators, Databases, Kafka and KafkaConnect clusters, Service Registry
and Data Grid cluster.

* [`02-load-enterprise-data.sh`](./02-load-enterprise-data.sh) - Load and initial sequence
of data into `enterprise` database.

* [`05-deploy-apps.sh`](./05-deploy-apps.sh) - Deploy the different applications of
the solution: business, streaming, backend, and dashboard.

* [`10-deploy-serverless-services.sh`](./10-deploy-serverless-services.sh) - Deploy the
the serverless components of the solution. This script will scale down the previous
applications.

* [`12-deploy-serverless-native-services.sh`](./12-deploy-serverless-native-services.sh) - Deploy
the Quarkus Native version of each component updating the previous serverless services created.

* [`98-query-enterprise-data.sh`](./98-query-enterprise-data.sh) - Summarize some data from the
`enterprise` database.

* [`99-update-regions.sh`](./99-update-regions.sh) - Sample script to update the `Regions`
table in the `enterprise` data base to start the consumption of the data and calculate
the values for each region by the `data-streaming` application.

The order to deploy successfully the solution is:

```shell
./01-deploy-infra.sh
./02-load-enterprise-data.sh
./05-deploy-apps.sh
```

To deploy the serverless version of the solution then you only need to execute:

```shell
./10-deploy-serverless-services.sh
./12-deploy-serverless-native-services.sh
```
