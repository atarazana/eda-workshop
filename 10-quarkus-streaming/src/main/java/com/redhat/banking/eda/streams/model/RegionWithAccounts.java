package com.redhat.banking.eda.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RegisterForReflection
public class RegionWithAccounts {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionWithAccounts.class);

    public Region region;
    public List<AccountWithMovements> accounts = new ArrayList<>();
    public int balanceRegion;
    public int accountsActive;
    public int accountsInactive;
    public int accountsClosed;

    public RegionWithAccounts addAccount(AccountAndRegion accountAndRegion) {
        LOGGER.info("Adding into Region {} the Account {}", accountAndRegion.region, accountAndRegion.account);

        region = accountAndRegion.region;
        accounts.add(accountAndRegion.account);

        // Update Balance of the region with the new data
        balanceRegion += accountAndRegion.account.accountBalance;
        // Update the account status counters
        switch (accountAndRegion.account.account.status) {
            case "ACTIVE":
                accountsActive++;
                break;

            case "INACTIVE":
                accountsInactive++;
                break;

            case "CLOSED":
                accountsClosed++;
                break;
        }

        return this;
    }

    public RegionWithAccounts removeAccount(AccountAndRegion accountAndRegion) {
        LOGGER.info("Removing from Region {} the Account {}", accountAndRegion.region, accountAndRegion.account);

        Iterator<AccountWithMovements> it = accounts.iterator();
        while (it.hasNext()) {
            AccountWithMovements a = it.next();
            if (a.account.id == accountAndRegion.account.account.id) {
                it.remove();
                // Update Region Account Balance
                balanceRegion -= accountAndRegion.account.accountBalance;

                // Update the account status counters
                switch (accountAndRegion.account.account.status) {
                    case "ACTIVE":
                        accountsActive--;
                        break;

                    case "INACTIVE":
                        accountsInactive--;
                        break;

                    case "CLOSED":
                        accountsClosed--;
                        break;
                }
                break;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RegionWithAccounts{");
        sb.append("region=").append(region);
        sb.append(", accounts=").append(accounts);
        sb.append(", balanceRegion=").append(balanceRegion);
        sb.append(", accountsActive=").append(accountsActive);
        sb.append(", accountsInactive=").append(accountsInactive);
        sb.append(", accountsClosed=").append(accountsClosed);
        sb.append('}');
        return sb.toString();
    }

}
