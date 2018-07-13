package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.IcoInfo;
import com.blockchain.store.playmarket.data.entities.IcoTeam;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class ResultAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("result") && jsonObject.get("status").getAsInt() == 200) {
                        jsonElement = jsonObject.get("result");
                    } else if (jsonObject.has("status") && jsonObject.get("status").getAsInt() != 200) {
                        throw new IOException(jsonObject.get("message").getAsString());
                    }
                }
                try {
                    return delegate.fromJsonTree(jsonElement);
                } catch (Exception e) {
                    return (T) new IcoInfo();
                }
            }
        }.nullSafe();
    }
}
