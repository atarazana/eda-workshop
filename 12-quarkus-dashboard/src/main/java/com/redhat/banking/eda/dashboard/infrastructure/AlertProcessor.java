package com.redhat.banking.eda.dashboard.infrastructure;

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

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class AlertProcessor {

    @Inject 
    @Remote("alerts")
    RemoteCache<String, Alert> cache;

    RemoteCacheManager remoteCacheManager;
    
    @Inject 
    AlertProcessor(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @Incoming("alerts")                                     
    @Outgoing("alerts-stream")                             
    @Broadcast                                              
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING) 
    public Alert process(Alert alert) {
        cache.put(alert.getId(), alert);
        return alert;
    }

}