# quarkus-streaming project

This project simulates backend applications consuming events, processing data
and generating new events to other elements of the architecture. Some data uses
Avro schemas coming from a Service Registry.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Getting Schemas from Service Registry

This project uses a set of schemas defined in the Service Registry. To download the latest
version of them and to generate the new classes:

```shell
./mvnw generate-sources -Papi-schemas-download \
  -Dapicurio.registry.url=http://$(oc get route -l app=eda-registry -o jsonpath='{.items[0].spec.host}')/apis/registry/v2
```

To generate the new classes:

```shell
./mvnw generate-sources
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 

```shell
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 

```shell
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/backend-2.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

### Pushing Native Image into a Container Image Registry

You can build a container image and push into a Container Image Registry using:

```shell script
./mvnw package -Pnative \
  -Dquarkus.native.container-build=true \
  -Dquarkus.container-image.build=true \
  -Dquarkus.container-image.builder=jib \
  -Dquarkus.container-image.push=true \
  -Dquarkus.container-image.registry=quay.io \
  -Dquarkus.container-image.group=eda-workshop \
  -Dquarkus.container-image.username=<username> \
  -Dquarkus.container-image.password='<password>'
```

## Deploying into OpenShift

To deploy the application using the Source-to-Image capabilities of OpenShift:

```shell
./mvnw package -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true
```

References:

* [SSLHandshakeException when deploying Quarkus application to OpenShift using S2I build](https://access.redhat.com/solutions/5397941)

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
