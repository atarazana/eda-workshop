package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.events.AggregateMetric;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class AggregateMetricGenerator {

    Logger logger = Logger.getLogger(AggregateMetricGenerator.class);

    private static String[] regions = {"Finland", "Russia", "Latvia", "Lithuania", "Poland"};

    private Random random = new Random();

    private static Double accumulatedBalanceFinland = 0.0;

    @ConfigProperty(name = "dummy.generator")
    String dummyGenerator;

    @Outgoing("generated-aggregate-metrics")
    public Multi<AggregateMetric> generate() {
        logger.info(">>>>>>>>>> AggregateMetric dummy.generator = " + dummyGenerator);

        if (dummyGenerator.equalsIgnoreCase("on")) {
            return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                    .onOverflow().drop()
                    .map(tick -> {
                        AggregateMetric metric = null;
                        int variant = random.nextInt(4);
                        switch (variant) {
                            case 0:
                                metric = generateBalanceByRegion();
                                break;
                            case 1:
                                metric = generateAccountsClosedByRegion();
                                break;
                            case 2:
                                metric = generateAccountsInactiveByRegion();
                                break;
                            case 3:
                                metric = generateAccountsActiveByRegion();
                                break;
                        }
                        return metric;
                    });
        }

        return Multi.createFrom().empty();
    }

    public AggregateMetric generateBalanceByRegion() {
        String region = regions[random.nextInt(regions.length)];

        Double balance = null;
        if (region == "Finland") {
            accumulatedBalanceFinland += random.nextInt(1000) + random.nextDouble();
            balance = accumulatedBalanceFinland;
            logger.info("Accumulated Balance Finland " + accumulatedBalanceFinland);
        } else {
            balance = random.nextInt(1000) + random.nextDouble();
        }

        AggregateMetric metric = AggregateMetric.newBuilder()
                .setName("Balance by Region")
                .setValue(balance)
                .setUnit("EUR")
                .setQualifier("COUNT")
                .setFrom("Region(" + region + ")")
                .setGroupByClause(region)
                .setTimestamp(Instant.now().toString())
                .build();

        logger.infov("AggregateMetric generated for %s with value %s", metric.getName(), metric.getValue());

        return metric;
    }

    public AggregateMetric generateAccountsClosedByRegion() {
        String region = regions[random.nextInt(regions.length)];

        AggregateMetric metric = AggregateMetric.newBuilder()
                .setName("Accounts Closed by Region")
                .setValue(random.nextInt(10) + 0.0)
                .setUnit("Accounts")
                .setQualifier("COUNT")
                .setFrom("Region(" + region + ")")
                .setGroupByClause(region)
                .setTimestamp(Instant.now().toString())
                .build();

        return metric;
    }

    public AggregateMetric generateAccountsInactiveByRegion() {
        String region = regions[random.nextInt(regions.length)];

        AggregateMetric metric = AggregateMetric.newBuilder()
                .setName("Accounts Inactive by Region")
                .setValue(random.nextInt(10) + 0.0)
                .setUnit("Accounts")
                .setQualifier("COUNT")
                .setFrom("Region(" + region + ")")
                .setGroupByClause(region)
                .setTimestamp(Instant.now().toString())
                .build();

        return metric;
    }

    public AggregateMetric generateAccountsActiveByRegion() {
        String region = regions[random.nextInt(regions.length)];

        AggregateMetric metric = AggregateMetric.newBuilder()
                .setName("Accounts Active by Region")
                .setValue(random.nextInt(10) + 0.0)
                .setUnit("Accounts")
                .setQualifier("COUNT")
                .setFrom("Region(" + region + ")")
                .setGroupByClause(region)
                .setTimestamp(Instant.now().toString())
                .build();

        return metric;
    }

}
