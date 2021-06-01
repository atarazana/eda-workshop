package com.redhat.banking.eda.dashboard.valueobjects;

import java.time.Instant;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class Alert {

    private final String id;
    private final String name;
    private final String variant;
    private final String definition;
    private final String expression;
    private final String duration;
    private final Instant timestamp;
    
    @ProtoFactory
    public Alert(String id, String name, String variant, String definition, String expression, String duration, Instant timestamp) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((variant == null) ? 0 : variant.hashCode());
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
        Alert other = (Alert) obj;
        if (definition == null) {
            if (other.definition != null)
                return false;
        } else if (!definition.equals(other.definition))
            return false;
        if (duration == null) {
            if (other.duration != null)
                return false;
        } else if (!duration.equals(other.duration))
            return false;
        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (variant == null) {
            if (other.variant != null)
                return false;
        } else if (!variant.equals(other.variant))
            return false;
        return true;
    }
    
}
