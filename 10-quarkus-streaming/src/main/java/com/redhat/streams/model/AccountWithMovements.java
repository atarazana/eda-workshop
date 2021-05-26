package com.redhat.streams.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RegisterForReflection
public class AccountWithMovements {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountWithMovements.class);

    public Account account;
    public int accountBalance = 0;
    public List<Movement> movements = new ArrayList<>();

    public AccountWithMovements addMovement(MovementAndAccount movementAndAccount) {
        LOGGER.info("Adding Movement into Account: {}", movementAndAccount);

        account = movementAndAccount.account;
        movements.add(movementAndAccount.movement);

        // Updating Account Balance
        accountBalance += movementAndAccount.movement.quantity;

        return this;
    }

    public AccountWithMovements removeMovement(MovementAndAccount movementAndAccount) {
        LOGGER.info("Removing Movement from Account: {}", movementAndAccount);

        Iterator<Movement> it = movements.iterator();
        while (it.hasNext()) {
            Movement m = it.next();
            if (m.id == movementAndAccount.movement.id) {
                it.remove();

                // Updating Account Balance
                accountBalance -= movementAndAccount.movement.quantity;

                break;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountWithMovements{");
        sb.append("account=").append(account);
        sb.append(", movements=").append(movements);
        sb.append('}');
        return sb.toString();
    }

}
