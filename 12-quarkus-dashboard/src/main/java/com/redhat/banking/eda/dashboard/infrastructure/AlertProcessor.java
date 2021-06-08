package com.redhat.banking.eda.dashboard.infrastructure;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.banking.eda.dashboard.valueobjects.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class AlertProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(AlertProcessor.class);

    private final MeterRegistry registry;

    @Inject 
    @Remote("alerts")
    RemoteCache<String, Alert> cache;

    RemoteCacheManager remoteCacheManager;
    
    @Inject 
    AlertProcessor(MeterRegistry registry, RemoteCacheManager remoteCacheManager) {
        this.registry = registry;
        this.remoteCacheManager = remoteCacheManager;
    }

    @Incoming("alerts")                                     
    @Outgoing("alerts-stream")                             
    @Broadcast                                              
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING) 
    public Alert process(Alert alert) {
        LOG.info("Processing Alert {}", alert);

        registry.counter("eda.workshop.alert.number", Tags.of("name", alert.getName())).increment();

        cache.put(alert.getId(), alert);
        return alert;
    }

}
