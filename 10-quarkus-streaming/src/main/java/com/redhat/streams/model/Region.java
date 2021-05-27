package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Region {

    public int id;
    public String code;
    public String name;
    public String description;

    public Region() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Region{");
        sb.append("id=").append(id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

