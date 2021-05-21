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
