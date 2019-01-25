package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenRepository {

    public static Observable<ArrayList<DaoToken>> getUserTokens() {
        ArrayList<DaoToken> userSavedTokens = getUserSavedTokens();
        ArrayList<Observable<String>> obsList = new ArrayList<>();
        for (DaoToken userSavedToken : userSavedTokens) {
            obsList.add(TransactionRepository.getUserTokenBalance(userSavedToken.address, AccountManager.getAddress().getHex()));
        }
        return Observable.from(obsList).concatMap(result -> result.observeOn(Schedulers.newThread())).toList().map(result -> {
            for (int i = 0; i <= userSavedTokens.size(); i++) {
                try {
                    userSavedTokens.get(i).balance = result.get(i);
                } catch (Exception e) {
                }
            }
            saveTokens(userSavedTokens);
            return userSavedTokens;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    public static ArrayList<DaoToken> getUserSavedTokens() {
        if (Hawk.contains(Constants.LOCAL_TOKEN_KEYS_V2)) {
            return Hawk.get(Constants.LOCAL_TOKEN_KEYS_V2);
        } else {
            return new ArrayList<>();
        }
    }


    public static void addToken(DaoToken token) {
        ArrayList<DaoToken> userTokens = getUserSavedTokens();
        for (DaoToken userToken : userTokens) {
            if (userToken.name.equalsIgnoreCase(token.name)) {
                return;
            }
        }
        userTokens.add(token);
        saveTokens(userTokens);
    }

    private static void saveTokens(ArrayList<DaoToken> userTokens) {
        Hawk.put(Constants.LOCAL_TOKEN_KEYS_V2, userTokens);
    }

    public static boolean isTokenAlreadyAdded(DaoToken token) {
        ArrayList<DaoToken> userTokens = getUserSavedTokens();
        for (DaoToken userToken : userTokens) {
            if (userToken.name.equalsIgnoreCase(token.name)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<DaoToken> deleteToken(DaoToken token) {
        ArrayList<DaoToken> newTokens = new ArrayList<>();
        ArrayList<DaoToken> userTokens = getUserSavedTokens();
        for (DaoToken userToken : userTokens) {
            if (!userToken.name.equalsIgnoreCase(token.name)) {
                newTokens.add(userToken);
            }
        }
        saveTokens(newTokens);
        return newTokens;
    }
}
