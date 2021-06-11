# Deploying Native Applications

Each application could be build as a native application and pushed into
a Image Registry. The following command will build locally
a native application and push into [quay.io](https://quay.io/organization/eda-workshop)
as Image Registry.

```shell
./mvnw package -Pnative \
  -Dquarkus.native.container-build=true \
  -Dquarkus.container-image.build=true \
  -Dquarkus.container-image.builder=jib \
  -Dquarkus.container-image.push=true \
  -Dquarkus.container-image.registry=quay.io \
  -Dquarkus.container-image.group=eda-workshop \
  -Dquarkus.container-image.username=<user> \
  -Dquarkus.container-image.password='<password>'
```

The following applications have a native application version:

* [data-streaming](https://quay.io/repository/eda-workshop/data-streaming)
* [backend](https://quay.io/repository/eda-workshop/backend)
* [dashboard](https://quay.io/repository/eda-workshop/dashboard)

## Deploying Native Applications

To deploy the applications as Knative Services:

```shell
oc apply -f service/
```

Or if you want to reuse the same services but using the native version, you could define
with image you want to use:

```shell
kn service update data-streaming-serverless --image quay.io/eda-workshop/data-streaming:1.0.0-SNAPSHOT
kn service update backend-serverless --image quay.io/eda-workshop/backend:1.0.0-SNAPSHOT
```

To deploy new `KafkaSource` source for these new services:

```shell
oc apply -f knative-eventing/kafka-source/
```

## Main References

[Quarkus - Building a Native Executable](https://quarkus.io/guides/building-native-image)
