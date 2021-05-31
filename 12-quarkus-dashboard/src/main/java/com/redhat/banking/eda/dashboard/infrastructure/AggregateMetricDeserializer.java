package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class AggregateMetricDeserializer  extends ObjectMapperDeserializer<AggregateMetric> {

    public AggregateMetricDeserializer() {
        super(AggregateMetric.class);
    }
}