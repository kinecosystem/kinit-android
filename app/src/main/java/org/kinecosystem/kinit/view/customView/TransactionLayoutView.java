package org.kinecosystem.kinit.view.customView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.airbnb.lottie.LottieAnimationView;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.navigation.Navigator.Destination;
import org.kinecosystem.kinit.repository.QuestionnaireRepository;
import org.kinecosystem.kinit.view.BaseActivity;

import javax.inject.Inject;


public class TransactionLayoutView extends ConstraintLayout {

    @BindingAdapter({"complete"})
    public static void updateComplete(TransactionLayoutView layoutView, boolean transactionComplete) {
        if (transactionComplete) {
            layoutView.updateComplete();
        }
    }

    @Inject
    QuestionnaireRepository questionnaireRepository;

    public TransactionLayoutView(@NonNull Context context) {
        super(context);
        init();
    }

    public TransactionLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        KinitApplication.getCoreComponent().inject(this);
    }


    private void updateComplete() {
        View transactionImage = findViewById(R.id.transaction_image);
        LottieAnimationView transactionAnim = findViewById(R.id.transaction_anim);
        View transactionTitle = findViewById(R.id.transaction_title);
        transactionImage.clearAnimation();

        AnimationDrawable animationDrawable = (AnimationDrawable) getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();

        transactionAnim.animate().alpha(0).setDuration(250L);
        transactionTitle.animate().alpha(0f).setDuration(250L);
        transactionImage.animate().setStartDelay(200L).translationYBy(transactionImage.getHeight()).setDuration(300L);

        View confetti = findViewById(R.id.confetti);
        View close = findViewById(R.id.close_text);
        close.setAlpha(0f);
        close.setVisibility(VISIBLE);
        confetti.setScaleX(0);
        confetti.setScaleY(0);
        confetti.setVisibility(VISIBLE);
        close.animate().alpha(1).setDuration(500L).setStartDelay(1850L + TransactionTextView.ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        close.setOnClickListener(view -> {
            if (getContext() != null) {
                BaseActivity activity = ((BaseActivity) getContext());
                Navigator navigator = new Navigator(activity);
                navigator.navigateTo(Destination.MAIN_SCREEN);
                questionnaireRepository.resetTaskState();
                activity.overridePendingTransition(
                        R.anim.fade_in, R.anim.fade_out);
                activity.finish();
            }
        });
        confetti.animate().setDuration(750L).setStartDelay(100 + TransactionTextView.ANIM_DURATION).scaleY(1.2f)
                .scaleX(1.2f).setListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                confetti.animate().setStartDelay(200).setDuration(600L).scaleY(0f)
                        .scaleX(0f).setInterpolator(new AccelerateInterpolator()).setListener(null);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        })
                .setInterpolator(new OvershootInterpolator(2f));
    }

}
