package com.redhat.banking.eda.cdc.converters;

import com.redhat.banking.eda.cdc.model.ClientDB;
import com.redhat.banking.eda.model.events.Alert;
import com.redhat.banking.eda.model.events.AlertVariant;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecordMetadata;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;

@ApplicationScoped
public class ClientDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDBConverter.class);

    @Inject
    @Channel("data-clients")
    Emitter<ClientDB> eventsClientEmitter;

    @Inject
    @Channel("eda-alerts")
    Emitter<Alert> alertEmitter;

    @Incoming("dbz-enterprise-clients")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public ClientDB process(ClientDB clientDB) {
        LOG.info("Consuming client from database: {}", clientDB);

        if (null != clientDB && ("c".equals(clientDB.op) || "u".equals(clientDB.op))) {
            // Identify the key
            OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                    .withKey(clientDB.id)
                    .build();

            eventsClientEmitter.send(Message.of(clientDB).addMetadata(metadata));

            LOG.info("{} data client from database {} published in data topic",
                    "c".equals(clientDB.op) ? "Created" : "Updated",
                    clientDB);

            if ("c".equals(clientDB.op)) {
                LOG.info("New Client alert {}", clientDB.email);

                Alert alert = Alert.newBuilder()
                        .setId(String.valueOf(clientDB.id))
                        .setVariant(AlertVariant.info)
                        .setName("Client Created")
                        .setDefinition("New client created with email " + clientDB.email)
                        .setExpression("")
                        .setDuration("1m")
                        .setLabels(new HashMap<>())
                        .setAnnotations(new HashMap<>())
                        .setTimestamp(Instant.now().toString())
                        .build();

                alertEmitter.send(alert);
            }
        }

        return clientDB;
    }

}
