package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class MovementAndAccount {

    public Movement movement;
    public Account account;

    public MovementAndAccount() {
    }

    public MovementAndAccount(Movement movement, Account account) {
        this.movement = movement;
        this.account = account;
    }

    public static MovementAndAccount create(Movement movement, Account account) {
        return new MovementAndAccount(movement, account);
    }

    public Movement movement() { return movement; }

    public Account account() {
        return account;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MovementAndAccount{");
        sb.append("movement=").append(movement);
        sb.append(", account=").append(account);
        sb.append('}');
        return sb.toString();
    }

}
