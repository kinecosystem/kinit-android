package org.kinecosystem.kinit.util;

import android.databinding.BindingAdapter;
import android.support.constraint.Guideline;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.view.customView.AnswerSelectedOverView;
import org.kinecosystem.kinit.view.customView.QuizAnswerView;


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

    @BindingAdapter("animatedFadeIn")
    public static void setVisibility(View view, boolean visible) {
        if (view != null) {
            if (visible) {
                view.setAlpha(0);
                view.setVisibility(View.VISIBLE);
                view.animate().alpha(1).setDuration(250);
            } else {
                view.setVisibility(View.INVISIBLE);
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
        if(showCorrect) {
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
}
