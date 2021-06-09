package com.redhat.banking.enterprise;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BusinessProducerResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/business")
          .then()
             .statusCode(200)
             .body(is("Hello Business Producer Application"));
    }

}