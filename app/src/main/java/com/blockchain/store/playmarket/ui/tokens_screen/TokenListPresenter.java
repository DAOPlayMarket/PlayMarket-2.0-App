package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.entities.TokenResponse;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenListPresenter implements TokenListContract.Presenter {
    private TokenListContract.View view;

    @Override
    public void init(TokenListContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllTokens() {
        TokenRepository.getUserTokens()
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onTokensReady);
        getTokenForBottomSheet();
    }

    public void getBottomSheetTokens() {
        getTokenForBottomSheet();
    }

    private void getTokenForBottomSheet() {
        new RestApi().getCustomUrlApi(Constants.TOKEN_URL)
                .getAllTokens()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBottomSheetTokensReady, this::onBottomSheetTokensError);
    }

    private void onBottomSheetTokensReady(TokenResponse tokenResponse) {
        view.onBottomSheetTokensReady(tokenResponse.tokens);
    }

    private void onBottomSheetTokensError(Throwable throwable) {
        view.onBottomSheetTokensFailed(throwable);
    }

    private void onTokensReady(ArrayList<Token> tokenResponse) {
        view.onTokensReady(tokenResponse);
    }

    @Override
    public void findToken(String address) {
        TransactionRepository.getTokenFullInfo(address, AccountManager.getAddress().getHex())
                .doOnSubscribe(() -> view.showBottomProgress(true))
                .doOnTerminate(() -> view.showBottomProgress(false))
                .subscribe(this::onNewTokenReady, this::onNewTokenFailed);
    }

    private void onNewTokenReady(Token token) {
        TokenRepository.addToken(token);
        view.onNewTokenReady(token);
    }

    private void onNewTokenFailed(Throwable throwable) {
        view.onNewTokenFailed(throwable);
    }

    public void getTokenBalance(Token token) {
        TransactionRepository.getUserTokenBalance(token.address, AccountManager.getAddress().getHex())
                .subscribe(result -> onTokenBalanceReady(token, result), this::onNewTokenFailed);
    }

    private void onTokenBalanceReady(Token token, String result) {
        token.balanceOf = result;
        view.onTokenBalanceReady(token);
    }

    public void addToken(Token token) {
        new TokenRepository().addToken(token);
        view.updateBottomSheetAdapter();
    }

    public void deleteToken(Token token) {
        new TokenRepository().deleteToken(token);
//        view.updateMainAdapter();
    }
}
