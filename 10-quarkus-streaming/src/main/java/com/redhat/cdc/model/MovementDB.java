package com.redhat.cdc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;

@RegisterForReflection
public class MovementDB {

    public String op;
    public int account_id;
    public int id;
    public Instant movement_date;
    public String description;
    public int quantity;

    public MovementDB() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MovementDB{");
        sb.append("op='").append(op).append('\'');
        sb.append(", account_id=").append(account_id);
        sb.append(", id=").append(id);
        sb.append(", movement_date=").append(movement_date);
        sb.append(", description='").append(description).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }

}
