package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountAndRegion {

    public AccountWithMovements account;
    public Region region;

    public AccountAndRegion() {
    }

    public AccountAndRegion(Region region, AccountWithMovements account) {
        this.region = region;
        this.account = account;
    }

    public static AccountAndRegion create(Region region, AccountWithMovements account) {
        return new AccountAndRegion(region, account);
    }

    public Region region() { return region; }

    public AccountWithMovements account() {
        return account;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountAndRegion{");
        sb.append("account=").append(account);
        sb.append(", region=").append(region);
        sb.append('}');
        return sb.toString();
    }

}
