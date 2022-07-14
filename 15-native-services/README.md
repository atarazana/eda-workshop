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
kn service update data-streaming-serverless --image quay.io/eda-workshop/data-streaming:2.10.1-SNAPSHOT
kn service update backend-serverless --image quay.io/eda-workshop/backend:2.10.1-SNAPSHOT
kn service update dashboard-serverless --image quay.io/eda-workshop/dashboard:2.10.1-SNAPSHOT
```

To deploy new `KafkaSource` source for these new services:

```shell
oc apply -f knative-eventing/kafka-source/
```

Now, these applications startup in few milliseconds, the faster ones in the architecture as :rocket:

* data-streaming application:

```text
2021-10-19 14:50:40,079 INFO  [io.quarkus] (main) data-streaming 2.1.3-SNAPSHOT native (powered by Quarkus 2.1.3.Final) started in 0.178s. Listening on: http://0.0.0.0:8080
```

* backend application:

```text
2021-10-19 14:50:52,030 INFO  [io.quarkus] (main) backend 2.1.3-SNAPSHOT native (powered by Quarkus 2.1.3.Final) started in 0.106s. Listening on: http://0.0.0.0:8080
```

* dashboard application:

```text
2021-10-19 14:52:01,190 INFO  [io.quarkus] (main) dashboard 2.1.3-SNAPSHOT native (powered by Quarkus 2.1.3.Final) started in 0.073s. Listening on: http://0.0.0.0:8080
```

## Main References

[Quarkus - Building a Native Executable](https://quarkus.io/guides/building-native-image)
