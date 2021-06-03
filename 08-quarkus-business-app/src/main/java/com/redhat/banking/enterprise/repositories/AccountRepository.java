package com.redhat.banking.enterprise.repositories;

import com.redhat.banking.enterprise.entities.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public List<Account> findByClientId(Integer clientId) {
        return find("client_id", clientId).list();
    }

}
