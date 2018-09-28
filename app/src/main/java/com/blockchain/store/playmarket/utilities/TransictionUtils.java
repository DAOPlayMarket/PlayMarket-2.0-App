package com.blockchain.store.playmarket.utilities;

import android.support.transition.AutoTransition;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.view.animation.AccelerateInterpolator;

import com.blockchain.store.playmarket.utilities.transitions.Recolor;

public class TransictionUtils {
    private static final int TRANSACTION_DURATION = 200;

    public static TransitionSet getTransactionSetForHistoryAdapter() {
        TransitionSet transitionSet = new TransitionSet();

        ChangeTransform changeTransform = new ChangeTransform();
        changeTransform.setDuration(TRANSACTION_DURATION);
        changeTransform.setInterpolator(new AccelerateInterpolator());

        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
        autoTransition.setDuration(TRANSACTION_DURATION);

        Recolor recolor = new Recolor();
        recolor.setDuration(TRANSACTION_DURATION);

        transitionSet.addTransition(autoTransition);
        transitionSet.addTransition(changeTransform);
        transitionSet.addTransition(recolor);
        return transitionSet;
    }
}
