package com.blockchain.store.playmarket.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontAwesomeTextView extends AppCompatTextView {

    private Context context;

    public FontAwesomeTextView(Context context) {
        super(context);
        this.context = context;
        setUp();
    }

    public FontAwesomeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setUp();
    }

    public FontAwesomeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setUp();
    }

    private void setUp() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/font_awesome.ttf");
        setTypeface(typeface);

    }
}
