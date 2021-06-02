package com.redhat.eda.listeners;

import com.redhat.eda.model.events.AggregateMetric;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.Instant;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RegionDomainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(RegionDomainProcessor.class);

    @Inject
    @Channel("eda-alerts")
    Emitter<String> alertsEmitter;

    @Inject
    @Channel("eda-aggregate-metrics")
    Emitter<String> aggregateMetricsEmitter;

    @Incoming("domain-regions")
    public CompletionStage consume(IncomingKafkaRecord<Integer, String> record) {
        LOG.info("Consuming domain region ({}) {}", record.getKey(), record.getPayload());

        // Processing Region Record
        JsonReader reader = Json.createReader(new StringReader(record.getPayload()));
        JsonObject regionDomain = reader.readObject();
        JsonObject regionData = regionDomain.getJsonObject("region");
        JsonArray accountsData = regionDomain.getJsonArray("accounts");

        // Domain Region Metrics
        String regionCode = regionData.getString("code");
        int accountsActive = regionDomain.getInt("accountsActive");
        int accountsInactive = regionDomain.getInt("accountsInactive");
        int accountsClosed = regionDomain.getInt("accountsClosed");
        int balanceRegion = regionDomain.getInt("balanceRegion");

        // Aggregated Metrics

        // Accounts Active
        AggregateMetric accountsActiveMetric = AggregateMetric.newBuilder()
                .setName("Accounts Active by Region")
                .setUnit("Accounts")
                .setValue(Double.valueOf(accountsActive))
                .setQualifier("COUNT")
                .setFrom("Region(" + regionCode + ")")
                .setGroupByClause(regionCode)
                .setTimestamp(Instant.now().toString())
                .build();

        // Accounts Inactive
        AggregateMetric accountsInactiveMetric = AggregateMetric.newBuilder()
                .setName("Accounts Inactive by Region")
                .setUnit("Accounts")
                .setValue(Double.valueOf(accountsInactive))
                .setQualifier("COUNT")
                .setFrom("Region(" + regionCode + ")")
                .setGroupByClause(regionCode)
                .setTimestamp(Instant.now().toString())
                .build();

        // Accounts Closed
        AggregateMetric accountsClosedMetric = AggregateMetric.newBuilder()
                .setName("Accounts Closed by Region")
                .setUnit("Accounts")
                .setValue(Double.valueOf(accountsInactive))
                .setQualifier("COUNT")
                .setFrom("Region(" + regionCode + ")")
                .setGroupByClause(regionCode)
                .setTimestamp(Instant.now().toString())
                .build();

        // Region Balance
        AggregateMetric balanceRegionMetric = AggregateMetric.newBuilder()
                .setName("Balance by Region")
                .setUnit("EUR")
                .setValue(Double.valueOf(balanceRegion))
                .setQualifier("COUNT")
                .setFrom("Region(" + regionCode + ")")
                .setGroupByClause(regionCode)
                .setTimestamp(Instant.now().toString())
                .build();

        LOG.info("Sending aggregated metric with Accounts active by Region {}", accountsActiveMetric);
        aggregateMetricsEmitter.send(accountsActiveMetric.toString());

        LOG.info("Sending aggregated metric with Accounts inactive by Region {}", accountsInactiveMetric);
        aggregateMetricsEmitter.send(accountsInactiveMetric.toString());

        LOG.info("Sending aggregated metric with Accounts closed by Region {}", accountsClosedMetric);
        aggregateMetricsEmitter.send(accountsClosedMetric.toString());

        LOG.info("Sending aggregated metric with Balance by Region {}", balanceRegionMetric);
        aggregateMetricsEmitter.send(balanceRegionMetric.toString());

        // Commit message
        return record.ack();
    }

}
