package com.redhat.banking.eda.model.dto;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.time.Instant;

public class AlertDTO {

    private final String id;
    private final String name;
    private final String variant;
    private final String definition;
    private final String expression;
    private final String duration;
    private final Instant timestamp;

    @ProtoFactory
    public AlertDTO(String id, String name, String variant, String definition,
                    String expression, String duration, Instant timestamp) {
        this.id = id;
        this.name = name;
        this.variant = variant;
        this.definition = definition;
        this.expression = expression;
        this.duration = duration;
        this.timestamp = timestamp;
    }

    @ProtoField(number = 1)
    public String getId() {
        return id;
    }

    @ProtoField(number = 2)
    public String getName() {
        return name;
    }

    @ProtoField(number = 3, defaultValue = "success")
    public String getVariant() {
        return variant;
    }

    @ProtoField(number = 4)
    public String getDefinition() {
        return definition;
    }

    @ProtoField(number = 5)
    public String getExpression() {
        return expression;
    }

    @ProtoField(number = 6)
    public String getDuration() {
        return duration;
    }

    @ProtoField(number = 9, defaultValue = "0")
    public Instant getTimestamp() {
        return timestamp;
    }

}
