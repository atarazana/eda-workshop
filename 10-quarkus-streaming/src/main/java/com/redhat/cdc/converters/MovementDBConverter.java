package com.redhat.cdc.converters;

import com.redhat.cdc.model.MovementDB;
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
public class MovementDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(MovementDBConverter.class);

    @Inject
    @Channel("data-movements")
    Emitter<MovementDB> movementDBEmitter;

    @Incoming("dbz-enterprise-movements")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public MovementDB process(MovementDB movementDB) {
        LOG.info("Consuming movement from database: {}", movementDB);

        if (null == movementDB) {
            return null;
        }

        if ("r".equals(movementDB.op)) {
            LOG.info("{} Movement from mainframe {}-{}-{}", ("r".equals(movementDB.op) ? "Read" : "Deleted"),
                    movementDB.account_id, movementDB.movement_date, movementDB.quantity);

            return null;
        }

        // Operation
        String op = movementDB.op;

        if ("c".equals(op) || "u".equals(op)) {
            // Identify the key
            OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                    .withKey(movementDB.id)
                    .build();

            movementDBEmitter.send(Message.of(movementDB).addMetadata(metadata));

            LOG.info("{} Movement from mainframe: {}-{}-{}", ("c".equals(movementDB.op) ? "Created" : "Updated"),
                    movementDB.account_id, movementDB.movement_date, movementDB.quantity);
        }

        return movementDB;
    }

}
