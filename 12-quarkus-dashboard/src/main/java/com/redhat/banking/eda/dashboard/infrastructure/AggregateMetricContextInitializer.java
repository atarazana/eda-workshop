package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { AggregateMetric.class }, schemaPackageName = "eda.workshop"
)
interface AggregateMetricContextInitializer extends SerializationContextInitializer {
}
