package com.redhat.banking.eda.backend;

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
            .body(is("{\"name\": \"backend\", \"data\": \"hello world\"}"));
    }

    @Test
    public void testPostEndpoint() {
        given()
            .body("{\"account_id\":74,\"description\":\"Incoming Transfer\",\"id\":1731,\"movement_date\":\"2022-07-13T00:00:00Z\",\"op\":\"c\",\"quantity\":879}")
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
            .body(is("{\"name\": \"backend\", \"data\": \"cloud event processed\", \"result\": \"ok\"}"));
    }

}
