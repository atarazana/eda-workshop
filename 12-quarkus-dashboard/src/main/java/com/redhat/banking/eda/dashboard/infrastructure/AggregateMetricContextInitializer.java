package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { AggregateMetric.class }, schemaPackageName = "aggregate_metric_sample")
interface AggregateMetricContextInitializer extends SerializationContextInitializer {
}
