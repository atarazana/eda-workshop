package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.dto.AggregateMetricDTO;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = {AggregateMetricDTO.class}, schemaPackageName = "eda.workshop"
)
interface AggregateMetricContextInitializer extends SerializationContextInitializer {
}
