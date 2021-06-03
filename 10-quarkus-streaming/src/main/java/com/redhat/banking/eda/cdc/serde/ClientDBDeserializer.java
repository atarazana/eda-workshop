package com.redhat.banking.eda.cdc.serde;

import com.redhat.banking.eda.cdc.model.ClientDB;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

@RegisterForReflection
public class ClientDBDeserializer extends JsonbDeserializer<ClientDB> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDBDeserializer.class);

    public ClientDBDeserializer() {
        super(ClientDB.class);
    }

    @Override
    public ClientDB deserialize(String topic, byte[] data) {
        JsonReader reader = Json.createReader(new StringReader(new String(data)));
        JsonObject jsonObject = reader.readObject();

        JsonObject payload = jsonObject.getJsonObject("payload");

        ClientDB clientDB = new ClientDB();
        String op = payload.getString("op");
        clientDB.op = op;
        if ("c".equals(op) || "u".equals(op)) {
            clientDB.id = payload.getJsonObject("after").getInt("id");
            clientDB.first_name = payload.getJsonObject("after").getString("first_name");
            clientDB.last_name = payload.getJsonObject("after").getString("last_name");
            clientDB.email = payload.getJsonObject("after").getString("email");

            LOG.info("{} Client from mainframe: {}", ("c".equals(clientDB.op) ? "Created" : "Updated"),
                    clientDB.email);
        } else {
            LOG.info("{} Client from mainframe", ("r".equals(clientDB.op) ? "Read" : "Deleted"));
        }

        return clientDB;
    }

}
