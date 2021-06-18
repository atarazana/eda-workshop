package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.dto.AggregateMetricDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class AggregateMetricDeserializer extends ObjectMapperDeserializer<AggregateMetricDTO> {

    public AggregateMetricDeserializer() {
        super(AggregateMetricDTO.class);
    }
}