package com.redhat.cdc.converters;

import com.redhat.cdc.model.RegionDB;
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
public class RegionDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(RegionDBConverter.class);

    @Inject
    @Channel("data-regions")
    Emitter<RegionDB> regionDBEmitter;

    @Incoming("dbz-enterprise-regions")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public RegionDB process(RegionDB regionDB) {
        LOG.info("Consuming region from database: {}", regionDB);

        if (null != regionDB && "c".equals(regionDB.op)) {
            // Identify the key
            OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                    .withKey(regionDB.id)
                    .build();

            regionDBEmitter.send(Message.of(regionDB).addMetadata(metadata));

            LOG.info("New data region created {} and published in data topic", regionDB);
        }

        return regionDB;
    }

}
