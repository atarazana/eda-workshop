package com.redhat.banking.eda.cdc.serde;

import com.redhat.banking.eda.cdc.model.RegionDB;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

@RegisterForReflection
public class RegionDBDeserializer extends JsonbDeserializer<RegionDB> {

    private static final Logger LOG = LoggerFactory.getLogger(RegionDBDeserializer.class);

    public RegionDBDeserializer() {
        super(RegionDB.class);
    }

    @Override
    public RegionDB deserialize(String topic, byte[] data) {
        JsonReader reader = Json.createReader(new StringReader(new String(data)));
        JsonObject jsonObject = reader.readObject();

        JsonObject payload = jsonObject.getJsonObject("payload");

        RegionDB regionDB = new RegionDB();

        regionDB.op = payload.getString("op");
        regionDB.code = payload.getJsonObject("after").getString("code");
        if ("c".equals(regionDB.op) || "u".equals(regionDB.op)) {
            regionDB.id = payload.getJsonObject("after").getInt("id");
            regionDB.name = payload.getJsonObject("after").getString("name");
            regionDB.description = payload.getJsonObject("after").getString("description");

            LOG.info("{} Region from database: {}", "c".equals(regionDB.op) ? "Created" : "Updated",
                    regionDB.code);
        } else {
            LOG.info("{} Region from database", "r".equals(regionDB.op) ? "Read" : "Deleted");
        }

        return regionDB;
    }

}
