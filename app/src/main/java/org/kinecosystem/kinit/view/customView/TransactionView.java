package org.kinecosystem.kinit.view.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import org.kinecosystem.kinit.R;


public class TransactionView extends FrameLayout {

    @BindingAdapter({"looping"})
    public static void updateLooping(TransactionView view, boolean shouldLoop) {
        if (shouldLoop) {
            view.startLoopAnimation();
        } else {
            view.stopLoopAnimation();
        }
    }

    private boolean looping = true;
    private View image;

    public TransactionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TransactionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TransactionView,
                0, 0);
        try {
            looping = a.getBoolean(R.styleable.TransactionView_looping, false);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.transaction_view, this, true);
        image = view.findViewById(R.id.transaction_image);
    }

    public void startLoopAnimation() {
        looping = true;
        image.setRotation(0);
        image.animate().rotation(360).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (looping) {
                    startLoopAnimation();
                } else {
                    image.clearAnimation();
                }
            }
        }).setDuration(1500).start();
    }

    public void stopLoopAnimation() {
        looping = false;
    }
}
