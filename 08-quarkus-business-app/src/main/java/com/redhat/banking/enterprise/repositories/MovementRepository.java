package com.redhat.banking.enterprise.repositories;

import com.redhat.banking.enterprise.entities.Client;
import com.redhat.banking.enterprise.entities.Movement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovementRepository implements PanacheRepository<Movement> {


}
