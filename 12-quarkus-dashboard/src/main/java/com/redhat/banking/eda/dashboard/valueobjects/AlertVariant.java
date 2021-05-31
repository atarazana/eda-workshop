package com.redhat.banking.eda.dashboard.valueobjects;

public enum AlertVariant {
    SUCCESS("success"), DANGER("danger"), WARNING("warning"), INFO("info"), DEFAULT("default");
    AlertVariant(String value) { this.value = value; }
    private final String value;
    public String value() { return value; }
}
