package com.redhat.eda.listeners;

import com.redhat.eda.model.events.AggregateMetric;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.infinispan.client.hotrod.RemoteCache;
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
public class ClientDomainProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDomainProcessor.class);

    @Inject
    @Channel("eda-alerts")
    Emitter<String> alertsEmitter;

    @Inject
    @Channel("eda-aggregate-metrics")
    Emitter<String> aggregateMetricsEmitter;

    @Incoming("domain-clients")
    public CompletionStage consume(IncomingKafkaRecord<Integer, String> record) {
        LOG.info("Consuming client ({}) {}", record.getKey(), record.getPayload());

        // Processing Client Record
        JsonReader reader = Json.createReader(new StringReader(record.getPayload()));
        JsonObject clientDomain = reader.readObject();
        JsonObject clientData = clientDomain.getJsonObject("client");
        JsonArray accountsData = clientDomain.getJsonArray("accounts");

        // Get accounts statuses and eval to send new events
        boolean accountsClosed = clientDomain.getBoolean("accountsClosed");
        boolean accountsInactivated = clientDomain.getBoolean("accountsInactivated");
        // Get Client data
        int clientId = clientData.getInt("id");
        String fullName = clientData.getString("first_name") + " " + clientData.getString("last_name");
        String email = clientData.getString("email");
        int accounts = accountsData.size();

        if (accountsInactivated) {
            AggregateMetric aggregateMetric = AggregateMetric.newBuilder()
                    .setName("Accounts Inactivated")
                    .setUnit("Accounts")
                    .setValue(Double.valueOf(accounts))
                    .setQualifier("COUNT")
                    .setFrom("ClientAccounts")
                    .setGroupByClause("Client")
                    .setTimestamp(Instant.now().toString())
                    .build();

            LOG.info("Sending aggregated metric with Accounts inactivated {}", aggregateMetric);

            aggregateMetricsEmitter.send(aggregateMetric.toString());
        }

        if (accountsClosed) {
            AggregateMetric aggregateMetric = AggregateMetric.newBuilder()
                    .setName("Accounts Closed")
                    .setUnit("Accounts")
                    .setValue(Double.valueOf(accounts))
                    .setQualifier("COUNT")
                    .setFrom("ClientAccounts")
                    .setGroupByClause("Client")
                    .setTimestamp(Instant.now().toString())
                    .build();

            LOG.info("Sending aggregated metric with Accounts closed {}", aggregateMetric);

            aggregateMetricsEmitter.send(aggregateMetric.toString());
        }

        // Commit message
        return record.ack();
    }

}
