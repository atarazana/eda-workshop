package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;

@RegisterForReflection
public class Movement {

    public int account_id;
    public int id;
    public Instant movement_date;
    public String description;
    public int quantity;

    public Movement() {
    }

}
