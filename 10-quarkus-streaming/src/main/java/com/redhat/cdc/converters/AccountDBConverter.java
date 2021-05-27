package com.redhat.cdc.converters;

import com.redhat.cdc.model.AccountDB;
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
public class AccountDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDBConverter.class);

    @Inject
    @Channel("data-accounts")
    Emitter<AccountDB> accountDBEmitter;

    @Incoming("dbz-enterprise-accounts")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public AccountDB process(AccountDB accountDB) {
        LOG.info("Consuming account from database: {}", accountDB);

        if (null == accountDB) {
            return null;
        }

        if ("r".equals(accountDB.op)) {
            LOG.info("{} Account from database {}-{}-{} with status {}",
                    "r".equals(accountDB.op) ? "Read" : "Deleted",
                    accountDB.region_code, accountDB.client_id, accountDB.sequence, accountDB.status);

            return null;
        }

        // Operation
        String op = accountDB.op;

        if ("c".equals(op) || "u".equals(op)) {
            // Identify the key
            OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                    .withKey(accountDB.id)
                    .build();

            accountDBEmitter.send(Message.of(accountDB).addMetadata(metadata));

            LOG.info("{} Account from database {}-{}-{} with status {}",
                    "c".equals(op) ? "Created" : "Updated",
                    accountDB.region_code, accountDB.client_id, accountDB.sequence, accountDB.status);
        }

        return accountDB;
    }

}
