package com.redhat.banking.eda.streams.model;

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
    public boolean accountsClosed = false;
    public boolean accountsInactivated = false;

    public ClientWithAccounts addAccount(AccountAndClient accountAndClient) {
        LOGGER.info("Adding account info {} to client {}", accountAndClient, client);

        client = accountAndClient.client;
        accounts.add(accountAndClient.account);

        // Eval accounts status
        evalAccountsStatus();

        return this;
    }

    public ClientWithAccounts removeAccount(AccountAndClient accountAndClient) {
        LOGGER.info("Removing account info {} from client {}", accountAndClient, client);

        Iterator<Account> it = accounts.iterator();
        while (it.hasNext()) {
            Account a = it.next();
            if (a.id == accountAndClient.account.id) {
                it.remove();
                break;
            }
        }

        // Eval accounts status
        evalAccountsStatus();

        return this;
    }

    private void evalAccountsStatus() {
        // Eval if all accounts are inactive
        accountsInactivated = true;
        for (Account account : accounts) {
            if (!"INACTIVE".equals(account.status)) {
                accountsInactivated = false;
                break;
            }
        }
        // Eval if all accounts are closed
        accountsClosed = true;
        for (Account account : accounts) {
            if (!"CLOSED".equals(account.status)) {
                accountsClosed = false;
                break;
            }
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClientWithAccounts{");
        sb.append("client=").append(client);
        sb.append(", accounts=").append(accounts);
        sb.append(", accountsClosed=").append(accountsClosed);
        sb.append(", accountsInactivated=").append(accountsInactivated);
        sb.append('}');
        return sb.toString();
    }

}
