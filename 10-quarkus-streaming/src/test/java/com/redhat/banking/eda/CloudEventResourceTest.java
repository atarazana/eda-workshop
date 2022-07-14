package com.redhat.banking.eda;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CloudEventResourceTest {

    @Test
    public void testGetEndpoint() {
        given()
        .when()
            .get("/")
        .then()
            .statusCode(200)
            .body(is("{\"name\": \"data-streaming\", \"data\": \"hello world\"}"));
    }

    @Test
    public void testPostEndpoint() {
        given()
            .body("{\"schemaId\":20,\"payload\":{\"before\":null,\"after\":{\"id\":251,\"client_id\":175,\"region_id\":104,\"region_code\":\"it\",\"sequence\":\"e64ae9f001\",\"status\":\"INACTIVE\"},\"source\":{\"version\":\"1.7.2.Final-redhat-00003\",\"connector\":\"mysql\",\"name\":\"dbserver02\",\"ts_ms\":1657696095000,\"snapshot\":\"false\",\"db\":\"enterprise\",\"sequence\":null,\"table\":\"accounts\",\"server_id\":223344,\"gtid\":null,\"file\":\"mysql-bin.000003\",\"pos\":298013,\"row\":0,\"thread\":null,\"query\":null},\"op\":\"c\",\"ts_ms\":1657696095026,\"transaction\":null}}")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header("ce-type", "dev.knative.kafka.event")
            .header("ce-specversion", "1.0")
            .header("ce-time", "2022-07-12T14:58:00.097Z")
            .header("ce-source", "/apis/v1/namespaces/eda-workshop/kafkasources/data-streaming-kafka-source#dbserver02.enterprise.movements")
            .header("ce-id", "partition:0/offset:1141")
        .when()
            .post("/")
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .body(is("{\"name\": \"data-streaming\", \"data\": \"cloud event processed\", \"result\": \"ok\"}"));
    }

}
