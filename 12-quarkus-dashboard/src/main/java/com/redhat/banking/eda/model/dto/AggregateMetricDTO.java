package com.redhat.banking.eda.model.dto;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.time.Instant;

public class AggregateMetricDTO {

    private final String name;
    private final Double value;
    private final String unit;
    private final String qualifier; // AVG / MAX / MIN / SUM / COUNT / etc
    private final String from;
    private final String groupByClause;
    private final Instant timestamp;

    @ProtoFactory
    public AggregateMetricDTO(String name, Double value, String unit, String qualifier,
                              String from, String groupByClause, Instant timestamp) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.qualifier = qualifier;
        this.from = from;
        this.groupByClause = groupByClause;
        this.timestamp = timestamp;
    }

    @ProtoField(number = 1)
    public String getName() {
        return name;
    }

    @ProtoField(number = 2)
    public Double getValue() {
        return value;
    }

    @ProtoField(number = 3)
    public String getUnit() {
        return unit;
    }

    @ProtoField(number = 4)
    public String getQualifier() {
        return qualifier;
    }

    @ProtoField(number = 5)
    public String getFrom() {
        return from;
    }

    @ProtoField(number = 6)
    public String getGroupByClause() {
        return groupByClause;
    }

    @ProtoField(number = 7, defaultValue = "0")
    public Instant getTimestamp() {
        return timestamp;
    }

}
