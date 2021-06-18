package com.redhat.banking.eda.model.dto;

public enum AlertVariantDTO {

    SUCCESS("success"), DANGER("danger"), WARNING("warning"), INFO("info"), DEFAULT("default");

    AlertVariantDTO(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }

}
