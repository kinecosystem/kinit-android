package org.kinecosystem.kinit.view.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import org.kinecosystem.kinit.R;

public class QuestionnaireProgressBar extends ProgressBar {

    private static final long DURATION = 500;
    private final int multiplier = getMax() / 100;
    private int progress;
    private boolean update = false;

    @BindingAdapter({"questionnaireProgress"})
    public static void updateProgress(QuestionnaireProgressBar view, int progress) {
        view.updateProgress(progress);
    }


    private void updateProgress(int progress){
        if (!update)
        {
            update = true;
            setProgress(progress * multiplier);
        }
        else
            animateProgress(progress * multiplier);
    }

    private void animateProgress(int newProgress) {
        ValueAnimator anim = ValueAnimator.ofInt(getProgress(), newProgress);
        anim.setDuration(DURATION);
        anim.setInterpolator(new DecelerateInterpolator(1.5f));
        anim.addUpdateListener(animation -> setProgress((int)animation.getAnimatedValue()));
        anim.start();
    }

    public QuestionnaireProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.TransactionTextView,
            0, 0);
        try {
            progress = a.getInteger(R.styleable.QuestionnaireProgressBar_progress, 0);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        setProgress(progress);
    }
}
