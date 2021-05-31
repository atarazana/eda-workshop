package com.redhat.banking.eda.dashboard;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.banking.eda.dashboard.valueobjects.Alert;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

@Path("/alerts")
public class AlertResource {

    @Inject
    @Channel("alerts-stream") Publisher<Alert> alerts; 

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType(MediaType.APPLICATION_JSON) 
    public Publisher<Alert> stream() { 
        return alerts;
    }
}