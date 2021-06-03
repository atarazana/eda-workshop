package com.redhat.banking.enterprise.entities;

public enum AccountStatusEnum {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), CLOSED("CLOSED");

    AccountStatusEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }

}
