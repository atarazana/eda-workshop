package com.redhat.banking.eda.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountAndRegion {

    //public Account account;
    public AccountWithMovements account;
    public Region region;

    public AccountAndRegion() {
    }

    //public AccountAndRegion(Account account, Region region) {
    public AccountAndRegion(AccountWithMovements account, Region region) {
        this.region = region;
        this.account = account;
    }

    //public static AccountAndRegion create(Account account, Region region) {
    public static AccountAndRegion create(AccountWithMovements account, Region region) {
        return new AccountAndRegion(account, region);
    }

    public Region region() { return region; }

    //public Account account() {
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
