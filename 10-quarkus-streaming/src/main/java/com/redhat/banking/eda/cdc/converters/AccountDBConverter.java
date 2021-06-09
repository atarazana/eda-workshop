package com.redhat.banking.eda.cdc.converters;

import com.redhat.banking.eda.cdc.model.AccountDB;
import com.redhat.eda.model.events.Alert;
import com.redhat.eda.model.events.AlertVariant;
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
public class AccountDBConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDBConverter.class);

    @Inject
    @Channel("data-accounts")
    Emitter<AccountDB> accountDBEmitter;

    @Inject
    @Channel("eda-alerts")
    //Emitter<Alert> alertEmitter;
    Emitter<String> alertEmitter;

    @Incoming("dbz-enterprise-accounts")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public AccountDB process(AccountDB accountDB) {
        LOG.info("Consuming account from database: {}", accountDB);

        if (null == accountDB) {
            return null;
        }

        if ("r".equals(accountDB.op)) {
            LOG.info("{} Account from database {}-{}-{}-{} with status {}",
                    "r".equals(accountDB.op) ? "Read" : "Deleted",
                    accountDB.region_id, accountDB.region_code, accountDB.client_id, accountDB.sequence,
                    accountDB.status);

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

            LOG.info("{} Account from database {}-{}-{}-{} with status {}",
                    "c".equals(op) ? "Created" : "Updated",
                    accountDB.region_id, accountDB.region_code, accountDB.client_id, accountDB.sequence,
                    accountDB.status);

            if ("INACTIVE".equals(accountDB.status)) {
                LOG.info("Account with status inactive", accountDB.sequence);

                Alert alert = Alert.newBuilder()
                        .setId(String.valueOf(accountDB.id))
                        .setVariant(AlertVariant.warning)
                        .setName("Account Inactivated")
                        .setDefinition("Account " + accountDB.region_code + "-" + accountDB.sequence +
                                " inactivated in region " + accountDB.region_code.toUpperCase())
                        .setDuration("2m")
                        .setExpression("")
                        .setLabels(new HashMap<>())
                        .setAnnotations(new HashMap<>())
                        .setTimestamp(Instant.now().toString())
                        .build();

                alertEmitter.send(alert.toString());
            }

            if ("CLOSED".equals(accountDB.status)) {
                LOG.info("Account with status closed", accountDB.sequence);

                Alert alert = Alert.newBuilder()
                        .setId(String.valueOf(accountDB.id))
                        .setVariant(AlertVariant.danger)
                        .setName("Account Closed")
                        .setDefinition("Account " + accountDB.region_code + "-" + accountDB.sequence +
                                " closed in region " + accountDB.region_code.toUpperCase())
                        .setDuration("3m")
                        .setExpression("")
                        .setLabels(new HashMap<>())
                        .setAnnotations(new HashMap<>())
                        .setTimestamp(Instant.now().toString())
                        .build();

                alertEmitter.send(alert.toString());
            }
        }

        return accountDB;
    }

}
