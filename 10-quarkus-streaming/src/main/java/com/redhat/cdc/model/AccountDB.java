package com.redhat.cdc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountDB {

    public String op;
    public int id;
    public int client_id;
    public int region_id;
    public String region_code;
    public String sequence;
    public String status;

    public AccountDB() {
    }

    public AccountDB(int id, int client_id, int region_id, String region_code, String sequence, String status) {
        this.id = id;
        this.client_id = client_id;
        this.region_id = region_id;
        this.region_code = region_code;
        this.sequence = sequence;
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountDB{");
        sb.append("id=").append(id);
        sb.append(", client_id=").append(client_id);
        sb.append(", region_id=").append(region_id);
        sb.append(", region_code='").append(region_code).append('\'');
        sb.append(", sequence='").append(sequence).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
