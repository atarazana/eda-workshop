package com.redhat.banking.eda.dashboard.infrastructure;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class AggregateMetricGenerator {
    private static String NAME = "Balance";
    private static String UNIT = "EUR";
    private static String QUALIFIER = "SUM";
    private static String FROM = "AccountBalance";
    private static String GROUP_BY_CLAUSE = "Department";

    private Random random = new Random();

    @Outgoing("generated-aggregate-metrics")
    public Multi<AggregateMetric> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .onOverflow().drop()
                .map(tick -> {
                    AggregateMetric metric = new AggregateMetric(
                        NAME, 
                        random.nextInt(1000) + random.nextDouble(), UNIT, QUALIFIER, FROM, GROUP_BY_CLAUSE, Instant.now());
                    return metric;
                });
    }

}