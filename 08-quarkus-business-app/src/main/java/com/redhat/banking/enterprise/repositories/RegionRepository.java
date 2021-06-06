package com.redhat.banking.enterprise.repositories;

import com.redhat.banking.enterprise.entities.Region;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegionRepository implements PanacheRepository<Region> {

    public Region findByCode(String code) {
        return find("code", code).firstResult();
    }

}
