package com.redhat.banking.eda.dashboard;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DashboardResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/dashboard")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}