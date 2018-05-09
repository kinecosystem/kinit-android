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

    public interface AnswerView{
        void answerShowAnimation(long delay);
    }

    @BindingAdapter({"answers", "onAnswer", "questionType", "isAlignedLeft"})
    public static void setAnswers(AnswersGridLayout view, List<Answer> answers, OnAnswerSelectedListener listener, String questionType, boolean isAlignedLeft) {
        view.setAnswers(answers, listener, questionType, isAlignedLeft);
    }

    public interface OnAnswerSelectedListener {
        boolean onAnswerSelected(Answer answer);
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
            for (int i = 0; i < getChildCount(); i++) {
                AnswerView questionAnswersView = (AnswerView) getChildAt(i);
                questionAnswersView.answerShowAnimation(i * FADE_IN_DURATION);
            }
        }, delay);
    }

    public void setAnswers(List<Answer> answers, OnAnswerSelectedListener answerClickListener, String questionType, boolean isAlignedLeft) {
        for (Answer answer : answers) {
            View view;
            if (Question.QuestionType.TEXT_IMAGE.getType().equals(questionType)) {
                view = new ImageTextAnswerView(getContext(), answer, answerClickListener);
            } else if (Question.QuestionType.TEXT_MULTIPLE.getType().equals(questionType)) {
                view = new MultipleAnswersView(getContext(), answer, answerClickListener);
            } else {
                view = new TextAnswerView(getContext(), answer, answerClickListener, isAlignedLeft);
            }
            addView(view);
        }
    }
}
