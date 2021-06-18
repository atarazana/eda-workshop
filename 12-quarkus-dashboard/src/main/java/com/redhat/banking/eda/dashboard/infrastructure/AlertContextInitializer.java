package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.model.dto.AlertDTO;
import com.redhat.banking.eda.model.events.AlertVariant;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = {AlertDTO.class}, schemaPackageName = "eda.workshop")
interface AlertContextInitializer extends SerializationContextInitializer {
}
