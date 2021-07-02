# event-schemas-api project

This project includes a set of API schemas to define the structure of data to share
in our Event-Driven Architecture. Many of these schemas use Avro to define the structure and to
facilitate to generate the POJO classes of them.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

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

You can then execute your native executable with: `./target/event-schemas-api-2.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Publishing Schemas into Service Registry

To register the schemas in Service Registry execute:

```shell
./mvnw generate-sources -Papi-schemas-register \
  -Dapicurio.registry.url=http://$(oc get route -l app=eda-registry -o jsonpath='{.items[0].spec.host}')/apis/registry/v2
```

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

## Starting a local Service Registry

To test locally this project you could start a local instance of Service Registry:

```shell
podman run --rm -it -p 8080:8080 apicurio/apicurio-registry-mem:2.0.1.Final
```

The Service Registry is available in [http://localhost:8080/](http://localhost:8080)
