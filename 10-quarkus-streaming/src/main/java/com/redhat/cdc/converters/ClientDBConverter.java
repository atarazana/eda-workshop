package com.redhat.cdc.converters;

import com.redhat.cdc.model.ClientDB;
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

@ApplicationScoped
public class ClientDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDBConverter.class);

    @Inject
    @Channel("data-clients")
    Emitter<ClientDB> eventsClientEmitter;

    @Incoming("dbz-enterprise-clients")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public ClientDB process(ClientDB clientDB) {
        LOG.info("Consuming client from database: {}", clientDB);

        if (null != clientDB && "c".equals(clientDB.op)) {
            // Identify the key
            OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                    .withKey(clientDB.id)
                    .build();

            eventsClientEmitter.send(Message.of(clientDB).addMetadata(metadata));

            LOG.info("New data client created {} and published in data topic", clientDB);
        }

        return clientDB;
    }

}
