package com.redhat.banking.enterprise.entities;

public enum MovementTypeEnum {
    INCOMING("Incoming Transfer"), OUTGOING("Outgoing Transfer");

    MovementTypeEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }

}
