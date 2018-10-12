package com.blockchain.store.playmarket.repositories;

import android.graphics.drawable.Drawable;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class TokenRepository {
    public ArrayList<Token> getUserTokens() {
        if (Hawk.contains(Constants.LOCAL_TOKEN_KEYS)) {
            return Hawk.get(Constants.LOCAL_TOKEN_KEYS);
        } else {
            return new ArrayList<>();
        }
    }


    public void addToken(Token token) {
        ArrayList<Token> userTokens = getUserTokens();
        userTokens.add(token);

        saveTokens(userTokens);
    }

    private void saveTokens(ArrayList<Token> userTokens) {
        Hawk.put(Constants.LOCAL_TOKEN_KEYS, userTokens);
    }
}
