package com.redhat.cdc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ClientDB {

    public String op;
    public int id;
    public String first_name;
    public String last_name;
    public String email;

    public ClientDB() {
    }

    public ClientDB(int id, String first_name, String last_name, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClientDB{");
        sb.append("id=").append(id);
        sb.append(", first_name='").append(first_name).append('\'');
        sb.append(", last_name='").append(last_name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
