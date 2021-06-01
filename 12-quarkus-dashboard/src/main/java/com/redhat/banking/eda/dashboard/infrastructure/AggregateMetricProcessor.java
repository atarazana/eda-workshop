package com.redhat.banking.eda.dashboard.infrastructure;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class AggregateMetricProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(AggregateMetricProcessor.class);

    @Incoming("aggregate-metrics")                                     
    @Outgoing("aggregate-metrics-stream")                             
    @Broadcast                                              
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING) 
    public AggregateMetric process(AggregateMetric metric) {
        LOG.info("Processing Aggregated Metric {}", metric);

        return metric;
    }

}
