package com.redhat.streams.model;

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
    public int balanceRegion;

    public RegionWithAccounts addAccount(AccountAndRegion accountAndRegion) {
        LOGGER.info("Adding Account Balance {} into Region {}", accountAndRegion.account.accountBalance, region.code);

        region = accountAndRegion.region;
        balanceRegion += accountAndRegion.account.accountBalance;

        return this;
    }

    public RegionWithAccounts removeAccount(AccountAndRegion accountAndRegion) {
        LOGGER.info("Removing Account Balance {} from Region {}", accountAndRegion.account.accountBalance, region.code);

        balanceRegion -= accountAndRegion.account.accountBalance;

        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RegionWithAccounts{");
        sb.append("region=").append(region);
        sb.append(", balanceRegion=").append(balanceRegion);
        sb.append('}');
        return sb.toString();
    }

}
