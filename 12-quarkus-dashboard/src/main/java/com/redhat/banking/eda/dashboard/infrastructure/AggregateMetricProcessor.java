package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.dto.AggregateMetricDTO;
import com.redhat.banking.eda.model.events.AggregateMetric;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class AggregateMetricProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(AggregateMetricProcessor.class);

    private final MeterRegistry registry;

    @Inject
    @Remote("aggregate-metrics")
    RemoteCache<String, AggregateMetricDTO> cache;

    RemoteCacheManager remoteCacheManager;

    @Inject
    AggregateMetricProcessor(MeterRegistry registry, RemoteCacheManager remoteCacheManager) {
        this.registry = registry;
        this.remoteCacheManager = remoteCacheManager;
    }

    @Incoming("aggregate-metrics")
    @Outgoing("aggregate-metrics-stream")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public AggregateMetricDTO process(AggregateMetric metric) {
        LOG.info("Processing Aggregated Metric {}", metric);

        if (null == metric) {
            LOG.info("*** Processing Null AggregateMetric");
            return null;
        }

        // Building new AggregateMetricDTO
        AggregateMetricDTO metricDTO = new AggregateMetricDTO(
                metric.getName(),
                metric.getValue(),
                metric.getUnit(),
                metric.getQualifier(),
                metric.getFrom(),
                metric.getGroupByClause(),
                Instant.parse(metric.getTimestamp()));

        registry.counter("eda.workshop.aggregate_metrics.received.total").increment();

        cache.put(metric.getTimestamp().toString(), metricDTO);

        if (metric.getName().matches("Accounts.*")) {
            registry.counter("eda.workshop.aggregate_metrics.accounts",
                    Tags.of("name", metric.getName(), "group_by_clause", metric.getGroupByClause())).increment();
        }

        registry.counter("eda.workshop.aggregate_metrics.proccessed.total").increment();

        return metricDTO;
    }

}
