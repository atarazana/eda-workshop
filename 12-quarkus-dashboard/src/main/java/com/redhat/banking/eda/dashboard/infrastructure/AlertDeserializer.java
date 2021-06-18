package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.events.Alert;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class AlertDeserializer extends ObjectMapperDeserializer<Alert> {

    public AlertDeserializer() {
        super(Alert.class);
    }
}