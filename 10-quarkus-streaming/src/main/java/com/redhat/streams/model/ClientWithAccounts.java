package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RegisterForReflection
public class ClientWithAccounts {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientWithAccounts.class);

    public Client client;
    public List<Account> accounts = new ArrayList<>();

    public ClientWithAccounts addAccount(AccountAndClient accountAndClient) {
        LOGGER.info("Adding: " + accountAndClient);

        client = accountAndClient.client;
        accounts.add(accountAndClient.account);

        return this;
    }

    public ClientWithAccounts removeAccount(AccountAndClient accountAndClient) {
        LOGGER.info("Removing: " + accountAndClient);

        Iterator<Account> it = accounts.iterator();
        while (it.hasNext()) {
            Account a = it.next();
            if (a.id == accountAndClient.account.id) {
                it.remove();
                break;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return "ClientWithAccounts [client=" + client + ", accounts=" + accounts + "]";
    }

}
