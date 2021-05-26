package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountAndClient {

    public Account account;
    public Client client;

    public AccountAndClient() {
    }

    public AccountAndClient(Account account, Client client) {
        this.account = account;
        this.client = client;
    }

    public static AccountAndClient create(Account account, Client client) {
        return new AccountAndClient(account, client);
    }

    public Account account() {
        return account;
    }

    @Override
    public String toString() {
        return "AccountAndClient [account=" + account + ", client=" + client + "]";
    }

}
