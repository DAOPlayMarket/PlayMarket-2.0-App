package com.blockchain.store.playmarket.utilities.transitions;


/*
 * Get from https://github.com/andkulikov/Transitions-Everywhere/blob/master/library/src/main/java/com/transitionseverywhere/Recolor.java*/

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This transition tracks changes during scene changes to the
 * {@link View#setBackground(android.graphics.drawable.Drawable) background}
 * property of its target views (when the background is a
 * {@link ColorDrawable}, as well as the
 * {@link TextView#setTextColor(android.content.res.ColorStateList)
 * color} of the text for target TextViews. If the color changes between
 * scenes, the color change is animated.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Recolor extends Transition {

    private static final String PROPNAME_BACKGROUND = "android:recolor:background";
    private static final String PROPNAME_TEXT_COLOR = "android:recolor:textColor";

    @Nullable
    public static final Property<TextView, Integer> TEXTVIEW_TEXT_COLOR;
    @Nullable
    public static final Property<ColorDrawable, Integer> COLORDRAWABLE_COLOR;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TEXTVIEW_TEXT_COLOR = new IntProperty<TextView>("") {

                @Override
                public void setValue(@NonNull TextView object, int value) {
                    object.setTextColor(value);
                }

                @NonNull
                @Override
                public Integer get(TextView object) {
                    return 0;
                }

            };
            COLORDRAWABLE_COLOR = new IntProperty<ColorDrawable>("") {
                @Override
                public void setValue(@NonNull ColorDrawable object, int value) {
                    object.setColor(value);
                }

                @NonNull
                @Override
                public Integer get(@NonNull ColorDrawable object) {
                    return object.getColor();
                }
            };
        } else {
            TEXTVIEW_TEXT_COLOR = null;
            COLORDRAWABLE_COLOR = null;
        }
    }

    public Recolor() {
    }

    public Recolor(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_TEXT_COLOR,
                    ((TextView) transitionValues.view).getCurrentTextColor());
        }
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues,
                                   @Nullable TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        final View view = endValues.view;
        Drawable startBackground = (Drawable) startValues.values.get(PROPNAME_BACKGROUND);
        Drawable endBackground = (Drawable) endValues.values.get(PROPNAME_BACKGROUND);
        ObjectAnimator bgAnimator = null;
        if (startBackground instanceof ColorDrawable && endBackground instanceof ColorDrawable) {
            ColorDrawable startColor = (ColorDrawable) startBackground;
            ColorDrawable endColor = (ColorDrawable) endBackground;
            if (startColor.getColor() != endColor.getColor()) {
                final int finalColor = endColor.getColor();
                endColor.setColor(startColor.getColor());
                bgAnimator = ObjectAnimator.ofInt(endColor, COLORDRAWABLE_COLOR, startColor.getColor(), finalColor);
                bgAnimator.setEvaluator(new ArgbEvaluator());
            }
        }
        ObjectAnimator textColorAnimator = null;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            int start = (Integer) startValues.values.get(PROPNAME_TEXT_COLOR);
            int end = (Integer) endValues.values.get(PROPNAME_TEXT_COLOR);
            if (start != end) {
                textView.setTextColor(end);
                textColorAnimator = ObjectAnimator.ofInt(textView, TEXTVIEW_TEXT_COLOR, start, end);
                textColorAnimator.setEvaluator(new ArgbEvaluator());
            }
        }
        return mergeAnimators(bgAnimator, textColorAnimator);
    }

    private static Animator mergeAnimators(@Nullable Animator animator1, @Nullable Animator animator2) {
        if (animator1 == null) {
            return animator2;
        } else if (animator2 == null) {
            return animator1;
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator1, animator2);
            return animatorSet;
        }
    }
}