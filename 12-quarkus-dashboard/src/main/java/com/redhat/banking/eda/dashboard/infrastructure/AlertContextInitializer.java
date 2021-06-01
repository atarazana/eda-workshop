package com.redhat.banking.eda.dashboard.infrastructure;

import com.redhat.banking.eda.dashboard.valueobjects.Alert;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { Alert.class }, schemaPackageName = "alert_sample")
interface AlertContextInitializer extends SerializationContextInitializer {
}
