package com.redhat.cdc.serde;

import com.redhat.cdc.model.MovementDB;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.Instant;

@RegisterForReflection
public class MovementDBDeserializer extends JsonbDeserializer<MovementDB> {

    private static final Logger LOG = LoggerFactory.getLogger(MovementDBDeserializer.class);

    public MovementDBDeserializer() {
        super(MovementDB.class);
    }

    @Override
    public MovementDB deserialize(String topic, byte[] data) {
        JsonReader reader = Json.createReader(new StringReader(new String(data)));
        JsonObject jsonObject = reader.readObject();

        JsonObject payload = jsonObject.getJsonObject("payload");

        MovementDB movementDB = new MovementDB();
        String op = payload.getString("op");
        movementDB.op = op;
        if ("c".equals(op) || "u".equals(op)) {
            movementDB.account_id = payload.getJsonObject("after").getInt("account_id");
            movementDB.id = payload.getJsonObject("after").getInt("id");
            // Convert days to seconds
            int days = payload.getJsonObject("after").getInt("movement_date");
            int secondsFromEpoch = days * 86400;
            movementDB.movement_date = Instant.ofEpochSecond(secondsFromEpoch);
            movementDB.description = payload.getJsonObject("after").getString("description");
            movementDB.quantity = payload.getJsonObject("after").getInt("quantity");

            LOG.info("{} Movement from mainframe: {}-{}-{}", ("c".equals(movementDB.op) ? "Created" : "Updated"),
                    movementDB.account_id, movementDB.movement_date, movementDB.quantity);
        } else {
            LOG.info("{} Movement from mainframe", ("r".equals(movementDB.op) ? "Read" : "Deleted"));
        }

        return movementDB;
    }

}
