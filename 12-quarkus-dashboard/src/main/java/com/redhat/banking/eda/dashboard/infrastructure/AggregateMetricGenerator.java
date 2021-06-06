package com.redhat.banking.eda.dashboard.infrastructure;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import io.smallrye.mutiny.Multi;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import org.jboss.logging.Logger;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class AggregateMetricGenerator {
    Logger logger = Logger.getLogger(AggregateMetricGenerator.class);

    private static String[] regions = {"Finland", "Russia", "Latvia", "Lithuania", "Poland"};

    private Random random = new Random();

    @ConfigProperty(name = "dummy.generator")
    String dummyGenerator;

    @Outgoing("generated-aggregate-metrics")
    public Multi<AggregateMetric> generate() {
        logger.info(">>>>>>>>>> dummy.generator = " + dummyGenerator);

        if (dummyGenerator.equalsIgnoreCase("on")) {
            return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
            .onOverflow().drop()
            .map(tick -> {
                AggregateMetric metric = null;
                int variant = random.nextInt(4);
                switch (variant) {
                    case 0:  metric = generateBalanceByRegion(); break;
                    case 1:  metric = generateAccountsClosedByRegion(); break;
                    case 2:  metric = generateAccountsInactiveByRegion(); break;
                    case 3:  metric = generateAccountsActiveByRegion(); break;
                }
                return metric;
            });
        }
        return null;
    }

    public AggregateMetric generateBalanceByRegion() {
        String region = regions[random.nextInt(regions.length)];
        AggregateMetric metric = new AggregateMetric(
                        "Balance by Region", 
                        random.nextInt(1000) + random.nextDouble(), 
                        "EUR", 
                        "COUNT", 
                        "Region(" + region + ")", 
                        region, Instant.now());
        return metric;
    }

    public AggregateMetric generateAccountsClosedByRegion() {
        String region = regions[random.nextInt(regions.length)];
        AggregateMetric metric = new AggregateMetric(
            "Accounts Closed by Region", 
            random.nextInt(10) + 0.0, 
            "Accounts", 
            "COUNT", 
            "Region(" + region + ")", 
            region, Instant.now());
        return metric;
    }

    public AggregateMetric generateAccountsInactiveByRegion() {
        String region = regions[random.nextInt(regions.length)];
        AggregateMetric metric = new AggregateMetric(
            "Accounts Inactive by Region", 
            random.nextInt(10) + 0.0, 
            "Accounts", 
            "COUNT", 
            "Region(" + region + ")", 
            region, Instant.now());
        return metric;
    }

    public AggregateMetric generateAccountsActiveByRegion() {
        String region = regions[random.nextInt(regions.length)];
        AggregateMetric metric = new AggregateMetric(
            "Accounts Active by Region", 
            random.nextInt(10) + 0.0, 
            "Accounts", 
            "COUNT", 
            "Region(" + region + ")", 
            region, Instant.now());
        return metric;
    }
}


