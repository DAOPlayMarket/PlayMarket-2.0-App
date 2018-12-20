package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenRepository {

    public static Observable<ArrayList<Token>> getUserTokens() {
        ArrayList<Token> userSavedTokens = getUserSavedTokens();
        ArrayList<Observable<String>> obsList = new ArrayList<>();
        for (Token userSavedToken : userSavedTokens) {
            obsList.add(TransactionRepository.getUserTokenBalance(userSavedToken.address, AccountManager.getAddress().getHex()));
        }
        return Observable.from(obsList).flatMap(result -> result.observeOn(Schedulers.newThread())).toList().map(result -> {
            for (int i = 0; i < userSavedTokens.size(); i++) {
                userSavedTokens.get(i).balanceOf = result.get(i);

            }
            saveTokens(userSavedTokens);
            return userSavedTokens;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    public static ArrayList<Token> getUserSavedTokens() {
        if (Hawk.contains(Constants.LOCAL_TOKEN_KEYS)) {
            return Hawk.get(Constants.LOCAL_TOKEN_KEYS);
        } else {
            return new ArrayList<>();
        }
    }


    public static void addToken(Token token) {
        ArrayList<Token> userTokens = getUserSavedTokens();
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
        ArrayList<Token> userTokens = getUserSavedTokens();
        for (Token userToken : userTokens) {
            if (userToken.name.equalsIgnoreCase(token.name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Token> deleteToken(Token token) {
        ArrayList<Token> newTokens = new ArrayList<>();
        ArrayList<Token> userTokens = getUserSavedTokens();
        for (Token userToken : userTokens) {
            if (!userToken.name.equalsIgnoreCase(token.name)) {
                newTokens.add(userToken);
            }
        }
        saveTokens(newTokens);
        return newTokens;
    }
}
