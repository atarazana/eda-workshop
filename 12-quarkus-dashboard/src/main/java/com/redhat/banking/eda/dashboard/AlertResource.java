package com.redhat.banking.eda.dashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.banking.eda.model.dto.AlertDTO;
import com.redhat.banking.eda.model.events.Alert;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.resteasy.reactive.RestSseElementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Multi;

@Path("/alerts")
public class AlertResource {
    private static final Logger LOG = LoggerFactory.getLogger(AlertResource.class);

    @Inject
    @Channel("alerts-stream") Multi<AlertDTO> alerts;

    @Inject 
    @Remote("alerts")
    RemoteCache<String, AlertDTO> cache;

    RemoteCacheManager remoteCacheManager;
    
    @Inject 
    AlertResource(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @GET
    public List<AlertDTO> allAlerts() {
        LOG.info("allAlerts");

        ArrayList<AlertDTO> alerts = new ArrayList<AlertDTO>();
        for (Map.Entry<String, AlertDTO> alert : cache.entrySet()) {
            alerts.add(alert.getValue());
        }

        LOG.info("size = " + cache.keySet().size());

        alerts.sort(Comparator.comparing(AlertDTO::getTimestamp).reversed());

        return alerts;
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @RestSseElementType(MediaType.APPLICATION_JSON) 
    public Multi<AlertDTO> stream() {
        return alerts;
    }

}
