package com.redhat.banking.eda.dashboard.infrastructure;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.banking.eda.dashboard.valueobjects.Alert;
import com.redhat.banking.eda.dashboard.valueobjects.AlertVariant;

import io.smallrye.mutiny.Multi;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class AlertGenerator {
    Logger logger = Logger.getLogger(AlertGenerator.class);

    private static String NAME = "Max Hourly Balance Exceeded";
    private static String DEFINITION = "Min Balance Exceeded 1000 EUR for a given Region";
    private static String EXPRESSION = "balance <= 1000";
    private static String DURATION = "1m";

    private Random random = new Random();

    @ConfigProperty(name = "dummy.generator")
    String dummyGenerator;
    
    @Outgoing("generated-alerts")
    public Multi<Alert> generate() {
        logger.info(">>>>>>>>>> dummy.generator = " + dummyGenerator);

        if (dummyGenerator.equalsIgnoreCase("on")) {
            return Multi.createFrom().ticks().every(Duration.ofSeconds(20))
                .onOverflow().drop()
                .map(tick -> {
                    UUID uuid = UUID.randomUUID();

                    // HashMap<String,String> labels = new HashMap<String,String>();
                    // labels.put("from", "dept-" + random.nextInt(10));

                    // HashMap<String,String> annotations = new HashMap<String,String>();
                    // annotations.put("location", "SPAIN");

                    int variant = random.nextInt(4);
                    String definition = DEFINITION;
                    AlertVariant variantValue = AlertVariant.DEFAULT;
                    switch (variant) {
                        case 0:  variantValue = AlertVariant.SUCCESS; definition = "Balance replenished"; break;
                        case 1:  variantValue = AlertVariant.INFO; definition = "All good"; break;
                        case 2:  variantValue = AlertVariant.WARNING; definition = "Min Balance about to be reached"; break;
                        case 3:  variantValue = AlertVariant.DANGER; definition = "Outstanding problem!"; break;
                    }

                    //Alert alert = new  Alert(uuid.toString(), NAME, variantValue.value(), DEFINITION, EXPRESSION, DURATION, labels, annotations, Instant.now());
                    Alert alert = new  Alert(uuid.toString(), NAME, variantValue.value(), definition, EXPRESSION, DURATION, Instant.now());
                    return alert;
                });
            }
        return null;
    }

}