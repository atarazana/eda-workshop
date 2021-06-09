package com.redhat.banking.eda;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DataStreamingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/data-streaming")
          .then()
             .statusCode(200)
             .body(is("Hello Data Streaming!"));
    }

}
