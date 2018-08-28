package org.kinecosystem.kinit.view.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;

import org.kinecosystem.kinit.R;

public class BuyButton extends android.support.v7.widget.AppCompatTextView {

    @BindingAdapter("android:visibility")
    public static void updateTransactionStatus(BuyButton view, boolean visible) {
        if (!visible) {
            view.animateOut();
        }
    }

    private void animateOut() {
        setText("");
        ValueAnimator anim = ValueAnimator.ofInt(getMeasuredWidth(), getMeasuredHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = val;
                setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(350);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animate().alpha(0).setDuration(150);
            }
        });
        anim.start();
    }

    public BuyButton(Context context) {
        super(context);
    }

    public BuyButton(Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void onBuy(int width) {
        setClickable(false);
        postDelayed(() -> setText(R.string.buying), 250);
        ValueAnimator anim = ValueAnimator.ofInt(getMeasuredWidth(), width);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = val;
            setLayoutParams(layoutParams);
        });
        anim.setInterpolator(new AnticipateInterpolator(1.5f));
        anim.setDuration(500);
        anim.start();
    }

    public void expand(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);
    }
}
