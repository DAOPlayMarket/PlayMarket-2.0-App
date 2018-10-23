package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class TokenRepository {
    public static ArrayList<Token> getUserTokens() {
        if (Hawk.contains(Constants.LOCAL_TOKEN_KEYS)) {
            return Hawk.get(Constants.LOCAL_TOKEN_KEYS);
        } else {
            return new ArrayList<>();
        }
    }


    public static void addToken(Token token) {
        ArrayList<Token> userTokens = getUserTokens();
        for (Token userToken : userTokens) {
            if (userToken.name.equalsIgnoreCase(token.name)) {
                return;
            }
        }
        userTokens.add(token);
        saveTokens(userTokens);
    }

    private static void saveTokens(ArrayList<Token> userTokens) {
        Hawk.put(Constants.LOCAL_TOKEN_KEYS, userTokens);
    }

    public static boolean isTokenAlreadyAdded(Token token) {
        ArrayList<Token> userTokens = getUserTokens();
        for (Token userToken : userTokens) {
            if (userToken.name.equalsIgnoreCase(token.name)) {
                return true;
            }
        }
        return false;
    }
}
