package com.redhat.banking.eda.dashboard;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/aggregate-metrics")
public class AggregateMetricResource {

    @Inject
    @Channel("aggregate-metrics-stream") Publisher<AggregateMetric> metrics; 

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType(MediaType.APPLICATION_JSON) 
    public Publisher<AggregateMetric> stream() { 
        return metrics;
    }
}