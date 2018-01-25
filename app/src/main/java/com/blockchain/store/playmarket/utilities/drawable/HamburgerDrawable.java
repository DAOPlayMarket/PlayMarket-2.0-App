package com.blockchain.store.playmarket.utilities.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;

import com.blockchain.store.playmarket.R;

/**
 * Created by samsheff on 21/09/2017.
 */

public class HamburgerDrawable extends DrawerArrowDrawable {

    public HamburgerDrawable(Context context){
        super(context);
        setColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        setBarLength(85.0f);
        setBarThickness(12.0f);
        setGapSize(15.0f);

    }
}
