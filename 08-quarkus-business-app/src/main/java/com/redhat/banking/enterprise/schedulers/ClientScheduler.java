package com.redhat.banking.enterprise.schedulers;

import com.redhat.banking.enterprise.entities.Account;
import com.redhat.banking.enterprise.entities.AccountStatusEnum;
import com.redhat.banking.enterprise.entities.Client;
import com.redhat.banking.enterprise.entities.Region;
import com.redhat.banking.enterprise.repositories.AccountRepository;
import com.redhat.banking.enterprise.repositories.ClientRepository;
import com.redhat.banking.enterprise.repositories.MovementRepository;
import com.redhat.banking.enterprise.repositories.RegionRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.scheduler.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@ApplicationScoped
public class ClientScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ClientScheduler.class);

    @Inject
    RegionRepository regionRepository;
    @Inject
    ClientRepository clientRepository;
    @Inject
    AccountRepository accountRepository;
    @Inject
    MovementRepository movementRepository;

    Random random = new Random();

    @Scheduled(cron = "{cron.expr.generate.clients}")
    @Transactional
    void generateClientWithAccounts() {
        // Random uuid
        UUID uuidClient = UUID.randomUUID();

        // creating a person
        Client client = new Client();
        client.first_name = "Client";
        client.last_name = uuidClient.toString();
        client.email = uuidClient.toString() + "@acme.org";

        // persist it
        clientRepository.persist(client);

        LOG.info("New Client {} generated with id {}", uuidClient.toString(), client.id);

        PanacheQuery<Region> regions = regionRepository.findAll();
        List<Region> regionList = regions.list();

        // Number of accounts to create
        int accountsLength = random.nextInt(4);

        // Create an Active Account
        for (int i = 0; i < accountsLength; i++) {
            // Region of the account
            Region region = regionList.get(random.nextInt(regionList.size()));

            Account account = new Account();
            account.client_id = client.id;
            account.region_id = region.id;
            account.region_code = region.code;
            account.sequence = UUID.randomUUID().toString().replace("-","").substring(0,10);
            account.status = AccountStatusEnum.values()[random.nextInt(AccountStatusEnum.values().length)].value();

            // persist it
            accountRepository.persist(account);

            LOG.info("Account[{}] {} generated with status {} in region {} for client {}",
                    i, account.sequence, account.status, account.region_code, client.id);
        }
    }

}
