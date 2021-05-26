package com.redhat.cdc.serde;

import com.redhat.cdc.model.AccountDB;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

@RegisterForReflection
public class AccountDBDeserializer extends JsonbDeserializer<AccountDB> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDBDeserializer.class);

    public AccountDBDeserializer() {
        super(AccountDB.class);
    }

    @Override
    public AccountDB deserialize(String topic, byte[] data) {
        JsonReader reader = Json.createReader(new StringReader(new String(data)));
        JsonObject jsonObject = reader.readObject();

        JsonObject payload = jsonObject.getJsonObject("payload");

        AccountDB accountDB = new AccountDB();
        String op = payload.getString("op");
        accountDB.op = op;
        if ("c".equals(op) || "u".equals(op)) {
            accountDB.id = payload.getJsonObject("after").getInt("id");
            accountDB.client_id = payload.getJsonObject("after").getInt("client_id");
            accountDB.region_code = payload.getJsonObject("after").getString("region_code");
            accountDB.sequence = payload.getJsonObject("after").getString("sequence");
            accountDB.status = payload.getJsonObject("after").getString("status");

            LOG.info("{} Account from mainframe: {}-{}-{}", ("c".equals(accountDB.op) ? "Created" : "Updated"),
                    accountDB.region_code, accountDB.client_id, accountDB.sequence);
        } else {
            LOG.info("{} Account from mainframe", ("r".equals(accountDB.op) ? "Read" : "Deleted"));
        }

        return accountDB;
    }

}
