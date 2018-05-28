package com.blockchain.store.playmarket.fabric;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public class EventsHelper {
    public static void logExceptions(Throwable throwable) {
        Answers.getInstance().logCustom(
                new CustomEvent("Login exceptions")
                        .putCustomAttribute("Exception", throwable.toString())
        );
    }
}
