package com.redhat.cdc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RegionDB {

    public String op;
    public int id;
    public String code;
    public String name;
    public String description;

    public RegionDB() {
    }

    public RegionDB(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RegionDB{");
        sb.append("op='").append(op).append('\'');
        sb.append(", id=").append(id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
