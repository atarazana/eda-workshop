# quarkus-streaming project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-streaming-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Deploying into OpenShift

To deploy the application using the Source-to-Image capabilities of OpenShift:

```shell script
./mvnw package -DskipTests=true -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true
```

To register the schemas in Service Registry execute:

```shell script
❯ ./mvnw generate-sources -Papicurio
```

## Deploying into OpenShift

To deploy the application using the Source-to-Image capabilities of OpenShift:

```shell script
./mvnw package -DskipTests=true -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true -Dquarkus.openshift.route.expose=true
```

References:

* [SSLHandshakeException when deploying Quarkus application to OpenShift using S2I build](https://access.redhat.com/solutions/5397941)

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)


oc get secret eda-infinispan-cert-secret -o jsonpath='{.data.tls\.crt}' | base64 --decode > src/main/resources/datagrid-tls.crt
keytool -list -keystore src/main/resources/truststore.jks -storepass password
keytool -delete -keystore src/main/resources/truststore.jks -storepass password -alias datagrid 
keytool -import -trustcacerts -alias datagrid -file src/main/resources/datagrid-tls.crt -keystore src/main/resources/truststore.jks -storepass password -noprompt

oc get secret eda-infinispan-cert-secret -o jsonpath='{.data.tls\.crt}' | base64 --decode > src/main/resources/datagrid-tls.crt
keytool -import -trustcacerts -alias datagrid -file src/main/resources/datagrid-tls.crt -keystore src/main/resources/truststore.jks -storepass password -noprompt