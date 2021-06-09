package com.redhat.banking.eda.backend;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BackendResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/backend")
          .then()
             .statusCode(200)
             .body(is("Hello Backend!"));
    }

}