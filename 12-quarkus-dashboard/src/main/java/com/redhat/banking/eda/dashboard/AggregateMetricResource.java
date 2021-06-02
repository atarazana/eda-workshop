package com.redhat.banking.eda.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.Remote;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/aggregate-metrics")
public class AggregateMetricResource {
    private static final Logger LOG = LoggerFactory.getLogger(AggregateMetricResource.class);

    @Inject
    @Channel("aggregate-metrics-stream") Publisher<AggregateMetric> metrics; 

    @Inject 
    @Remote("aggregate-metrics")
    RemoteCache<String, AggregateMetric> cache;

    RemoteCacheManager remoteCacheManager;
    
    @Inject 
    AggregateMetricResource(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @GET
    public List<AggregateMetric> allAggregateMetrics() { 
        LOG.info("allAggregateMetrics");

        ArrayList<AggregateMetric> aggregateMetrics = new ArrayList<AggregateMetric>();
        for (Map.Entry<String, AggregateMetric> alert : cache.entrySet()) {
            aggregateMetrics.add(alert.getValue());
        }

        LOG.info("size = " + cache.keySet().size());

        return aggregateMetrics;  
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    @SseElementType(MediaType.APPLICATION_JSON) 
    public Publisher<AggregateMetric> stream() { 
        return metrics;
    }
}