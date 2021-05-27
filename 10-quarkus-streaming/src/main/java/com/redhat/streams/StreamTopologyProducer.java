package com.redhat.streams;

import com.redhat.streams.model.Account;
import com.redhat.streams.model.AccountAndClient;
import com.redhat.streams.model.AccountWithMovements;
import com.redhat.streams.model.Client;
import com.redhat.streams.model.ClientWithAccounts;
import com.redhat.streams.model.Movement;
import com.redhat.streams.model.MovementAndAccount;
import io.quarkus.kafka.client.serialization.JsonbSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ClientProducer {

    /*
     * Data topics to analyze
     */
    static final String DATA_CLIENTS_TOPIC = "eda.data.clients";
    static final String DATA_ACCOUNTS_TOPIC = "eda.data.accounts";
    static final String DATA_MOVEMENTS_TOPIC = "eda.data.movements";

    /*
     * Domain Topics with information consolidated
     */
    static final String EDA_DOMAIN_CLIENTS_TOPIC = "eda.events.domain.clients";
    static final String EDA_DOMAIN_ACCOUNTS_TOPIC = "eda.events.domain.accounts";
    static final String EDA_DOMAIN_REGIONS_TOPIC = "eda.events.domain.regions";

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // Serde Definitions for DBZ Accounts Topic
        //Serde<Integer> accountKeySerde = DebeziumSerdes.payloadJson(Integer.class);
        //accountKeySerde.configure(Collections.emptyMap(), true);
        //Serde<Account> accountSerde = DebeziumSerdes.payloadJson(Account.class);
        //accountSerde.configure(Collections.singletonMap("from.field", "after"), false);
        // Serde Definitions for Data Accounts Topic
        Serde<Integer> accountKeySerde = new Serdes.IntegerSerde();
        JsonbSerde<Account> accountSerde = new JsonbSerde<>(Account.class);

        // Serde Definitions for DBZ Clients Topic
        //Serde<Integer> clientsKeySerde = DebeziumSerdes.payloadJson(Integer.class);
        //clientsKeySerde.configure(Collections.emptyMap(), true);
        //Serde<Client> clientsSerde = DebeziumSerdes.payloadJson(Client.class);
        //clientsSerde.configure(Collections.singletonMap("from.field", "after"), false);
        // Serde Definitions for Data Clients Topic
        Serde<Integer> clientsKeySerde = new Serdes.IntegerSerde();
        JsonbSerde<Client> clientsSerde = new JsonbSerde<>(Client.class);

        // Serde Definitions for Data Movements Topic
        Serde<Integer> movementsKeySerde = new Serdes.IntegerSerde();
        JsonbSerde<Movement> movementsSerde = new JsonbSerde<>(Movement.class);

        // Serde Definitions for Temporal Data Topics
        JsonbSerde<AccountAndClient> accountAndClientSerde = new JsonbSerde<>(AccountAndClient.class);
        JsonbSerde<ClientWithAccounts> clientWithAccountsSerde = new JsonbSerde<>(ClientWithAccounts.class);
        JsonbSerde<MovementAndAccount> movementAndAccountSerde = new JsonbSerde<>(MovementAndAccount.class);
        JsonbSerde<AccountWithMovements> accountWithMovementsSerde = new JsonbSerde<>(AccountWithMovements.class);

        // KTable to consume messages from Data Account Topic
        KTable<Integer, Account> accounts = builder.table(
                DATA_ACCOUNTS_TOPIC,
                Consumed.with(accountKeySerde, accountSerde)
        );

        // KTable to consume messages from Data Client Topic
        KTable<Integer, Client> clients = builder.table(
                DATA_CLIENTS_TOPIC,
                Consumed.with(clientsKeySerde, clientsSerde)
        );

        // KTable to consume messages from Data Movement Topic
        KTable<Integer, Movement> movements = builder.table(
                DATA_MOVEMENTS_TOPIC,
                Consumed.with(movementsKeySerde, movementsSerde)
        );

        // KTable to join data from Clients and Accounts KTables
        // It creates the Domain Client Model
        KTable<Integer, ClientWithAccounts> clientWithAccountsKTable = accounts.join(
                clients,
                account -> account.client_id,
                AccountAndClient::new,
                Materialized.with(Serdes.Integer(), accountAndClientSerde)
        )
                .groupBy(
                        (accountId, accountAndClient) -> KeyValue.pair(accountAndClient.client.id, accountAndClient),
                        Grouped.with(Serdes.Integer(), accountAndClientSerde)
                )
                .aggregate(
                        ClientWithAccounts::new,
                        (clientId, accountAndClient, aggregate) -> aggregate.addAccount(accountAndClient),
                        (clientId, accountAndClient, aggregate) -> aggregate.removeAccount(accountAndClient),
                        Materialized.with(Serdes.Integer(), clientWithAccountsSerde)
                );

        // Publish Domain Client Data into Domain Topic
        clientWithAccountsKTable
                .toStream()
                .to(
                        EDA_DOMAIN_CLIENTS_TOPIC,
                        Produced.with(Serdes.Integer(), clientWithAccountsSerde)
                );

        // KTable to join data from Accounts and Movements KTables
        // It creates the Domain Account Model
        KTable<Integer, AccountWithMovements> accountWithMovementsKTable = movements.join(
                accounts,
                movement -> movement.account_id,
                MovementAndAccount::new,
                Materialized.with(Serdes.Integer(), movementAndAccountSerde)
        )
                .groupBy(
                        (movementId, movementAndAccount) -> KeyValue.pair(movementAndAccount.account.id, movementAndAccount),
                        Grouped.with(Serdes.Integer(), movementAndAccountSerde)
                )
                .aggregate(
                        AccountWithMovements::new,
                        (accountId, movementAndAccount, aggregate) -> aggregate.addMovement(movementAndAccount),
                        (accountId, movementAndAccount, aggregate) -> aggregate.removeMovement(movementAndAccount),
                        Materialized.with(Serdes.Integer(), accountWithMovementsSerde)
                );

        // Publish Domain Account Data into Domain Topic
        accountWithMovementsKTable
                .toStream()
                .to(
                        EDA_DOMAIN_ACCOUNTS_TOPIC,
                        Produced.with(Serdes.Integer(), accountWithMovementsSerde)
                );

        return builder.build();
    }

}
