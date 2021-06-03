package com.redhat.banking.enterprise.repositories;

import com.redhat.banking.enterprise.entities.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {

    public Client findByEmail(String email) {
        return find("email", email).firstResult();
    }

}
