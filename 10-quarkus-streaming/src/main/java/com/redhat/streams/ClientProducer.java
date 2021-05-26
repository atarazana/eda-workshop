package com.redhat.streams;

import com.redhat.streams.model.Account;
import com.redhat.streams.model.AccountAndClient;
import com.redhat.streams.model.Client;
import com.redhat.streams.model.ClientWithAccounts;
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

    static final String DATA_CLIENTS_TOPIC = "eda.data.clients";
    static final String DATA_ACCOUNTS_TOPIC = "eda.data.accounts";
    static final String EDA_DOMAIN_CLIENTS_TOPIC = "eda.domain.clients";

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

        // Serde Definitions for Temporal Data Topics
        JsonbSerde<AccountAndClient> accountAndClientSerde = new JsonbSerde<>(AccountAndClient.class);
        JsonbSerde<ClientWithAccounts> clientWithAccountsSerde = new JsonbSerde<>(ClientWithAccounts.class);

        KTable<Integer, Account> accounts = builder.table(
                DATA_ACCOUNTS_TOPIC,
                Consumed.with(accountKeySerde, accountSerde)
        );

        KTable<Integer, Client> clients = builder.table(
                DATA_CLIENTS_TOPIC,
                Consumed.with(clientsKeySerde, clientsSerde)
        );

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

        clientWithAccountsKTable
                .toStream()
                .to(
                        EDA_DOMAIN_CLIENTS_TOPIC,
                        Produced.with(Serdes.Integer(), clientWithAccountsSerde)
                );

        return builder.build();
    }

}
