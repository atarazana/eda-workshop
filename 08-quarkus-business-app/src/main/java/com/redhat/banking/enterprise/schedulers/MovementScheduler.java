package com.redhat.banking.enterprise.schedulers;

import com.redhat.banking.enterprise.entities.Account;
import com.redhat.banking.enterprise.entities.Client;
import com.redhat.banking.enterprise.entities.Movement;
import com.redhat.banking.enterprise.entities.MovementTypeEnum;
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
import java.time.Instant;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class MovementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(MovementScheduler.class);

    private static final int MAX_TRANSFER = 10000;
    private static final int MIN_TRANSFER = 10000;

    RegionRepository regionRepository;
    @Inject
    ClientRepository clientRepository;
    @Inject
    AccountRepository accountRepository;
    @Inject
    MovementRepository movementRepository;

    Random random = new Random();

    @Scheduled(cron = "{cron.expr.generate.movements}")
    @Transactional
    void generateMovements() {
        // Get list of clients
        PanacheQuery<Client> clientPanacheQuery = clientRepository.findAll();
        List<Client> clients = clientPanacheQuery.list();

        // Choose one client randomly
        Client client = clients.get(random.nextInt(clients.size()));

        // Get accounts of the client
        List<Account> accounts = accountRepository.findByClientId(client.id);

        if (accounts.isEmpty()) {
            LOG.info("Client {} has no accounts to generate movements", client.email);

            return;
        }

        for (Account account : accounts) {
            // Add a random number of movements with random outgoing or incoming
            int movementsSize = random.nextInt(10);

            LOG.info("Generating {} movements for the client {} in account {}-{}-{}",
                    movementsSize,
                    client.email,
                    account.region_code, account.sequence, account.status);

            for (int i = 0; i < movementsSize; i++) {
                Movement movement = new Movement();
                movement.account_id = account.id;
                movement.movement_date = Instant.now();

                // Random Incoming or Outgoing movement
                MovementTypeEnum movementTypeEnum = MovementTypeEnum.values()[random.nextInt(MovementTypeEnum.values().length)];
                movement.description = movementTypeEnum.value();
                if (MovementTypeEnum.INCOMING.equals(movementTypeEnum)) {
                    movement.quantity = random.nextInt(MAX_TRANSFER);
                } else {
                    movement.quantity = random.nextInt(MIN_TRANSFER) * -1;
                }

                // persist it
                movementRepository.persist(movement);

                LOG.info("Movement[{}] {} quantity by {} in account {}-{}-{} for client {}",
                        i, movement.quantity, movement.description,
                        account.region_code, account.sequence, account.status,
                        client.email);
            }
        }
    }

}
