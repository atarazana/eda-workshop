package com.redhat.banking.eda.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CloudEventResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudEventResource.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String servingEndpoint() {
        LOGGER.info("CloudEvent's @GET method invoked.");

        return "{\"name\": \"backend\", \"data\": \"hello world\"}";
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processCloudEvent(@Context HttpHeaders httpHeaders,
                                      String cloudEventJSON) {
        LOGGER.info("CloudEventResource's @POST method invoked. Processing new CloudEvent");

        LOGGER.info("CloudEventSpec({}/{}) from {}-{} with payload: {}",
                httpHeaders.getHeaderString("ce-type"),
                httpHeaders.getHeaderString("ce-specversion"),
                httpHeaders.getHeaderString("ce-source"),
                httpHeaders.getHeaderString("ce-id"),
                cloudEventJSON
        );

        return Response.status(Response.Status.OK).entity(
                        "{\"name\": \"backend\", \"data\": \"cloud event processed\", \"result\": \"ok\"}")
                .build();
    }

}
