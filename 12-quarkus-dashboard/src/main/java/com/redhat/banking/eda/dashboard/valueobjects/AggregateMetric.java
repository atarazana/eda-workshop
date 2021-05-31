package com.redhat.banking.eda.dashboard.valueobjects;

import java.time.Instant;

public class AggregateMetric {
    private final String name;
    private final Double value;
    private final String unit;
    private final String qualifier; // AVG / MAX / MIN / SUM / COUNT
    private final String from;    
    private final String groupByClause;
    private final Instant timestamp;
    
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

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getFrom() {
        return from;
    }

    public String getGroupByClause() {
        return groupByClause;
    }

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
