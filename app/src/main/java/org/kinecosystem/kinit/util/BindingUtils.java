package org.kinecosystem.kinit.util;

import android.databinding.BindingAdapter;
import android.support.constraint.Guideline;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.view.customView.AnswerSelectedOverView;


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
}
