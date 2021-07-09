package com.redhat.banking.eda.dashboard;

//import com.redhat.banking.eda.dashboard.resources.CacheResource;
//import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
//@QuarkusTestResource(CacheResource.class)
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
