package com.blockchain.store.dao.database.database_converters;

import android.arch.persistence.room.TypeConverter;

import com.blockchain.store.dao.database.model.Vote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class VotesConverter {

    @TypeConverter
    public static ArrayList<Vote> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Vote>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Vote> votes) {
        Gson gson = new Gson();
        return gson.toJson(votes);
    }
}

