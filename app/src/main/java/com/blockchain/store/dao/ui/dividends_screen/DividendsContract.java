package com.blockchain.store.dao.ui.dividends_screen;

import com.blockchain.store.dao.data.entities.DaoToken;

public class DividendsContract {

    interface View {

        void onTokensComplete();

        void onTokenNext(DaoToken token);

        void onTokensError(Throwable throwable);

        void transferFailed(Throwable throwable);

        void receive(String result);
    }

    interface Presenter {

        void init(View view);

        void getDividendsTokens();

        void runSingleTx(DaoToken daoToken);

        void runDoubleTx(DaoToken daoToken);

    }

}
