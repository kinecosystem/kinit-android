package org.kinecosystem.kinit.util;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.server.api.EcoApplicationsApi;
import org.kinecosystem.kinit.view.customView.AnswerSelectedOverView;
import org.kinecosystem.kinit.view.customView.EcoApplicationCategoryView;
import org.kinecosystem.kinit.view.customView.PagesIndicatorsView;
import org.kinecosystem.kinit.view.customView.QuizAnswerView;

import java.util.List;


public class BindingUtils {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            ImageUtils.loadImageIntoView(imageView.getContext(), url, imageView);
        }
    }

    @BindingAdapter("android:layout_marginStart")
    public static void setLayoutMarginStart(View view, float layoutMarginStart) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMarginStart((int) layoutMarginStart);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_constraintGuide_begin")
    public static void setLayoutConstraintGuideBegin(View view, float layoutGuideBegin) {
        ((Guideline) view).setGuidelineBegin((int) layoutGuideBegin);
    }

    @BindingAdapter("layout_constraintGuide_end")
    public static void setLayoutConstraintGuideEnd(View view, float layoutGuideBegin) {
        ((Guideline) view).setGuidelineBegin((int) layoutGuideBegin);
    }

    @BindingAdapter("android:layout_marginEnd")
    public static void setLayoutMarginEnd(View view, float layoutMarginEnd) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMarginEnd((int) layoutMarginEnd);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setLayoutMarginTop(View view, float layoutMarginTop) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = (int) layoutMarginTop;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("android:background")
    public static void setBgColor(View view, String color) {
        if (view != null && color != null) {
            view.setBackgroundColor(Color.parseColor(color));
        }
    }

    @BindingAdapter("app:layout_constraintTop_toBottomOf")
    public static void updateConstrainTopToBottom(View view, @IdRes int resId) {
        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(view.getId(), ConstraintSet.TOP, resId, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);
    }

    @BindingAdapter(value = {"bind:animatedFadeIn", "bind:animationDelay"}, requireAll = false)
    public static void setVisibility(View view, boolean visible, int delay) {
        if (view != null) {
            if (visible) {
                view.setAlpha(0);
                view.setVisibility(View.VISIBLE);
                view.animate().setStartDelay(delay).alpha(1).setDuration(750);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @BindingAdapter(value = {"bind:animatedFadeOut", "bind:animationDelay"}, requireAll = false)
    public static void setInVisibility(View view, boolean invisible, int delay) {
        if (view != null) {
            if (invisible) {
                view.setAlpha(1);
                view.setVisibility(View.VISIBLE);
                view.animate().setStartDelay(delay).alpha(0).setDuration(500).withEndAction(() -> view.setVisibility(View.INVISIBLE));
            } else {
                view.setAlpha(1);
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @BindingAdapter("visibilityOn")
    public static void setVisibilityOn(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter({"bind:run", "bind:script"})
    public static void callJavaScript(WebView webView, boolean run, String script) {
        if (run) {
            webView.evaluateJavascript(script, null);
        }
    }

    @BindingAdapter("overSelectionListener")
    public static void addOverListener(AnswerSelectedOverView view, AnswerSelectedOverView.OnSelectionListener listener) {
        if (listener != null) {
            view.setListener(listener);
        }
    }

    @BindingAdapter("answer")
    public static void answer(AnswerSelectedOverView view, Answer answer) {
        if (answer != null) {
            view.setAnswer(answer);
        }
    }

    @BindingAdapter({"answer", "correct"})
    public static void setAnswer(QuizAnswerView view, Answer answer, Boolean isCorrect) {
        view.setAnswer(answer);
        view.setCorrect(isCorrect);
    }

    @BindingAdapter("showCorrect")
    public static void showCorrect(QuizAnswerView view, Boolean showCorrect) {
        if (showCorrect) {
            view.showCorrect(false);
        }
    }

    @BindingAdapter("reward")
    public static void setReward(QuizAnswerView view, Integer reward) {
        view.setReward(reward);
    }


    @BindingAdapter("onSelectionListener")
    public static void setSelectionListener(QuizAnswerView view, QuizAnswerView.OnSelectionListener listener) {
        if (listener != null) {
            view.setListener(listener);
        }
    }

    @BindingAdapter("onType")
    public static void setInputListener(EditText view, TextWatcher listener) {
        if (listener != null) {
            view.addTextChangedListener(listener);
        }
    }

    @BindingAdapter("onSpinnerItemSelected")
    public static void setSpinnerItemListener(Spinner view, AdapterView.OnItemSelectedListener listener) {
        if (listener != null) {
            view.setOnItemSelectedListener(listener);
        }
    }

    @BindingAdapter("visibilityOn")
    public static void setVisibilityOn(EditText view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
            view.postDelayed(() -> view.requestFocus(), 250);
            GeneralUtils.openKeyboard(view.getContext(), view);
            view.requestFocus();
        } else {
            view.setVisibility(View.GONE);
            view.postDelayed(() -> view.clearFocus(), 250);
            GeneralUtils.closeKeyboard(view.getContext(), view);
            view.setText("");
        }
    }

    @BindingAdapter("indicatorCount")
    public static void setIndicatorCount(PagesIndicatorsView view, Integer count) {
        view.addCircles(count);
    }

    @BindingAdapter("pageSelected")
    public static void setIndicatorSelected(PagesIndicatorsView view, Integer page) {
        view.setPageSelected(page);
    }

    @BindingAdapter("ecoAppsCategories")
    public static void addEcoAppCategories(LinearLayout container, List<EcoApplicationsApi.AppsCategory> appCategories) {
        container.removeAllViews();
        for (int i = 0; i < appCategories.size(); i++) {
            container.addView(new EcoApplicationCategoryView(container.getContext(), appCategories.get(i).getId()));
        }
    }

    @BindingAdapter({"changeBackground"})
    public static void changeBackground(ViewGroup view, boolean changeBackground) {
        if (changeBackground) {
            if (view.getBackground() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                animationDrawable.setEnterFadeDuration(3000);
                animationDrawable.setExitFadeDuration(1500);
                animationDrawable.start();
            }
        }
    }

}
