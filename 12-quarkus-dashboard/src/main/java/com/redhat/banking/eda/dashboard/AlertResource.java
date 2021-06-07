package com.redhat.banking.eda.dashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.banking.eda.dashboard.valueobjects.Alert;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.Remote;

@Path("/alerts")
public class AlertResource {
    private static final Logger LOG = LoggerFactory.getLogger(AlertResource.class);

    @Inject
    @Channel("alerts-stream") Publisher<Alert> alerts; 

    @Inject 
    @Remote("alerts")
    RemoteCache<String, Alert> cache;

    RemoteCacheManager remoteCacheManager;
    
    @Inject 
    AlertResource(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @GET
    public List<Alert> allAlerts() { 
        LOG.info("allAlerts");

        ArrayList<Alert> alerts = new ArrayList<Alert>();
        for (Map.Entry<String, Alert> alert : cache.entrySet()) {
            alerts.add(alert.getValue());
        }

        LOG.info("size = " + cache.keySet().size());

        alerts.sort(Comparator.comparing(Alert::getTimestamp).reversed());

        return alerts;
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType(MediaType.APPLICATION_JSON) 
    public Publisher<Alert> stream() { 
        return alerts;
    }

    // class InstantSorter implements Comparator<Alert> {
    //     @Override
    //     public int compare(Alert o1, Alert o2) {
    //         return o2.getTimestamp() > o1.getTimestamp();
    //     }
    // }
}