package com.redhat.banking.eda.dashboard.valueobjects;

import java.time.Instant;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class AggregateMetric {
    private final String name;
    private final Double value;
    private final String unit;
    private final String qualifier; // AVG / MAX / MIN / SUM / COUNT / etc
    private final String from;    
    private final String groupByClause;
    private final Instant timestamp;
    
    @ProtoFactory
    public AggregateMetric(String name, Double value, String unit, String qualifier, String from, String groupByClause,
            Instant timestamp) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((groupByClause == null) ? 0 : groupByClause.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((qualifier == null) ? 0 : qualifier.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AggregateMetric other = (AggregateMetric) obj;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (groupByClause == null) {
            if (other.groupByClause != null)
                return false;
        } else if (!groupByClause.equals(other.groupByClause))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (qualifier == null) {
            if (other.qualifier != null)
                return false;
        } else if (!qualifier.equals(other.qualifier))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (unit == null) {
            if (other.unit != null)
                return false;
        } else if (!unit.equals(other.unit))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
