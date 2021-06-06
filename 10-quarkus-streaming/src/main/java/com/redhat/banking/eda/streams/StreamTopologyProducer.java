package com.redhat.banking.eda.streams;

import com.redhat.banking.eda.streams.model.Account;
import com.redhat.banking.eda.streams.model.AccountAndClient;
import com.redhat.banking.eda.streams.model.AccountAndRegion;
import com.redhat.banking.eda.streams.model.AccountWithMovements;
import com.redhat.banking.eda.streams.model.Client;
import com.redhat.banking.eda.streams.model.ClientWithAccounts;
import com.redhat.banking.eda.streams.model.Movement;
import com.redhat.banking.eda.streams.model.MovementAndAccount;
import com.redhat.banking.eda.streams.model.Region;
import com.redhat.banking.eda.streams.model.RegionWithAccounts;
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
public class StreamTopologyProducer {

    /*
     * Data topics to analyze
     */
    static final String DATA_CLIENTS_TOPIC = "eda.data.clients";
    static final String DATA_ACCOUNTS_TOPIC = "eda.data.accounts";
    static final String DATA_MOVEMENTS_TOPIC = "eda.data.movements";
    static final String DATA_REGIONS_TOPIC = "eda.data.regions";

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

        // Serde Definitions for Data Regions Topic
        Serde<Integer> regionsKeySerde = new Serdes.IntegerSerde();
        JsonbSerde<Region> regionsSerde = new JsonbSerde<>(Region.class);

        // Serde Definitions for Temporal Data Topics
        JsonbSerde<AccountAndClient> accountAndClientSerde = new JsonbSerde<>(AccountAndClient.class);
        JsonbSerde<ClientWithAccounts> clientWithAccountsSerde = new JsonbSerde<>(ClientWithAccounts.class);
        JsonbSerde<MovementAndAccount> movementAndAccountSerde = new JsonbSerde<>(MovementAndAccount.class);
        JsonbSerde<Integer> accountWithMovementsKeySerde = new JsonbSerde<>(Integer.class);
        JsonbSerde<AccountWithMovements> accountWithMovementsSerde = new JsonbSerde<>(AccountWithMovements.class);
        JsonbSerde<AccountAndRegion> accountAndRegionSerde = new JsonbSerde<>(AccountAndRegion.class);
        JsonbSerde<RegionWithAccounts> regionWithAccountsSerde = new JsonbSerde<>(RegionWithAccounts.class);

        // KTable to consume messages from Data Client Topic
        KTable<Integer, Client> clients = builder.table(
                DATA_CLIENTS_TOPIC,
                Consumed.with(clientsKeySerde, clientsSerde)
        );

        // KTable to consume messages from Data Account Topic
        KTable<Integer, Account> accounts = builder.table(
                DATA_ACCOUNTS_TOPIC,
                Consumed.with(accountKeySerde, accountSerde)
        );

        // KTable to consume messages from Data Region Topic
        KTable<Integer, Region> regions = builder.table(
                DATA_REGIONS_TOPIC,
                Consumed.with(regionsKeySerde, regionsSerde)
        );

        /*
         * Aggregation data of Clients and Accounts
         */

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

        /*
         * Aggregation data of Accounts and Movements
         */

        // KTable to consume messages from Data Movement Topic
        KTable<Integer, Movement> movements = builder.table(
                DATA_MOVEMENTS_TOPIC,
                Consumed.with(movementsKeySerde, movementsSerde)
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

        /*
         * Aggregation data from Regions and Accounts
         */

        // KTable to consume messages from Data Movement Topic
        KTable<Integer, AccountWithMovements> accountsWithMovements = builder.table(
                EDA_DOMAIN_ACCOUNTS_TOPIC,
                Consumed.with(new Serdes.IntegerSerde(), accountWithMovementsSerde)
        );

        // KTable to join data from Regions and Accounts with Movements KTables
        KTable<Integer, RegionWithAccounts> regionWithAccountsKTable = accountsWithMovements.join(
                regions,
                account -> account.account.region_id,
                AccountAndRegion::new,
                Materialized.with(Serdes.Integer(), accountAndRegionSerde)
        )
                .groupBy(
                        (accountId, accountAndRegion) -> KeyValue.pair(accountAndRegion.region.id, accountAndRegion),
                        Grouped.with(Serdes.Integer(), accountAndRegionSerde)
                )
                .aggregate(
                        RegionWithAccounts::new,
                        (regionId, accountAndRegion, aggregate) -> aggregate.addAccount(accountAndRegion),
                        (regionId, accountAndRegion, aggregate) -> aggregate.removeAccount(accountAndRegion),
                        Materialized.with(Serdes.Integer(), regionWithAccountsSerde)
                );

        // Publish Domain Region Data into Domain Topic
        regionWithAccountsKTable
                .toStream()
                .to(
                        EDA_DOMAIN_REGIONS_TOPIC,
                        Produced.with(Serdes.Integer(), regionWithAccountsSerde)
                );

        // Original code joining data.regions and data.accounts (worked)
//        // KTable to join data from Regions and Accounts KTables
//        KTable<Integer, RegionWithAccounts> regionWithAccountsKTable = accounts.join(
//                regions,
//                account -> account.region_id,
//                AccountAndRegion::new,
//                Materialized.with(Serdes.Integer(), accountAndRegionSerde)
//        )
//                .groupBy(
//                        (accountId, accountAndRegion) -> KeyValue.pair(accountAndRegion.region.id, accountAndRegion),
//                        Grouped.with(Serdes.Integer(), accountAndRegionSerde)
//                )
//                .aggregate(
//                        RegionWithAccounts::new,
//                        (regionId, accountAndRegion, aggregate) -> aggregate.addAccount(accountAndRegion),
//                        (regionId, accountAndRegion, aggregate) -> aggregate.removeAccount(accountAndRegion),
//                        Materialized.with(Serdes.Integer(), regionWithAccountsSerde)
//                );
//
//        // Publish Domain Region Data into Domain Topic
//        regionWithAccountsKTable
//                .toStream()
//                .to(
//                        EDA_DOMAIN_REGIONS_TOPIC,
//                        Produced.with(Serdes.Integer(), regionWithAccountsSerde)
//                );

        // Build and return Kafka Streams Topology
        return builder.build();
    }

}
