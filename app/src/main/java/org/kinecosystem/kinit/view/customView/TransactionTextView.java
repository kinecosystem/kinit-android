package org.kinecosystem.kinit.view.customView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import org.kinecosystem.kinit.R;


public class TransactionTextView extends android.support.v7.widget.AppCompatTextView {

    @BindingAdapter({"balance", "complete"})
    public static void updateBalance(TransactionTextView view, String balance, boolean complete) {
        view.updateBalance(balance, complete);
    }

    public static final long ANIM_DURATION = 2500;

    private void updateBalance(String balance, boolean complete) {
        if (complete) {
            if (getText() == null || getText().length() == 0) {
                setText(balance.toString());
            }
            animateBalance(balance);
        } else {
            setText(balance.toString());
        }
    }

    public void animateBalance(String newBalance) {
        ValueAnimator anim = ValueAnimator
            .ofInt(Integer.valueOf(getText().toString()), Integer.valueOf(newBalance));
        anim.setDuration(ANIM_DURATION);
        anim.setInterpolator(new DecelerateInterpolator(1.5f));
        anim.addUpdateListener(animation -> setText(animation.getAnimatedValue().toString()));
        anim.start();
        ValueAnimator animator = ObjectAnimator.ofFloat(this, "textSize", 50, 54);
        animator.setInterpolator(new OvershootInterpolator(3f));
        animator.setDuration(ANIM_DURATION);

        animator.start();
    }

    public TransactionTextView(@NonNull Context context) {
        super(context);
        init(context);
    }


    private String text = "0";

    public TransactionTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.TransactionTextView,
            0, 0);
        try {
            text = a.getString(R.styleable.TransactionTextView_balance);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        setText(text);
    }

}
