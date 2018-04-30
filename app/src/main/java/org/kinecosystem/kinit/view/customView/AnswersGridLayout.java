package org.kinecosystem.kinit.view.customView;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;

import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.model.earn.Question;

import java.util.List;

public class AnswersGridLayout extends GridLayout {
    public static final long SHOW_ANSWERS_DELAY = 750l;
    public static final long FADE_IN_DURATION = 200l;

    @BindingAdapter({"answers", "onAnswer", "questionType", "isAlignedLeft"})
    public static void setAnswers(AnswersGridLayout view, List<Answer> answers, OnAnswerListener onAnswer, String questionType, boolean isAlignedLeft) {
        view.setAnswers(answers, onAnswer, questionType, isAlignedLeft);
    }

    public interface OnAnswerListener {
        boolean onAnswered(Answer answer);
    }

    public AnswersGridLayout(Context context) {
        super(context);
        init(context);
    }

    public AnswersGridLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswersGridLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        fadeInAnswers(SHOW_ANSWERS_DELAY);
    }

    private void fadeInAnswers(long delay) {
        postDelayed(() -> {
            if (getChildAt(0) instanceof TextAnswerView) {
                for (int i = 0; i < getChildCount(); i++) {
                    TextAnswerView questionAnswersView = (TextAnswerView) getChildAt(i);
                    questionAnswersView.fadeIn(i * FADE_IN_DURATION);
                }
            }
        }, delay);
    }

    public void setAnswers(List<Answer> answers, OnAnswerListener listener, String questionType, boolean isAlignedLeft) {
        for (Answer answer : answers) {
            View view;
            if (Question.QuestionType.TEXT_IMAGE.getType().equals(questionType)) {
                view = new ImageTextAnswerView(getContext(), answer, listener);
            } else {
                view = new TextAnswerView(getContext(), answer, listener, isAlignedLeft);
            }
            addView(view);
        }
    }
}
