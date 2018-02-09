package com.blockchain.store.playmarket.ui.new_user_welcome_activity;


import android.content.Context;

public class NewUserWelcomeContract {

    interface View{

    }

    interface Presenter{

        void init(NewUserWelcomeContract.View view, Context context);

        // Объявление метода сохранения копии ключа на устройстве.
        void saveKeyOnDevice(String accountKey);

        // Объявление метода сохранения копии ключа на электронной почте.
        void saveKeyOnEmail();

        // Объявление метода сохранения копии ключа в облаке.
        void saveKeyOnCloud();
    }
}
