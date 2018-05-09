package org.kinecosystem.kinit.view.customView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.model.earn.Answer;

public class MultipleAnswersView extends FrameLayout implements AnswersGridLayout.AnswerView{
    private Answer answer;
    private int imageRes, textColor, backgroundButton;
    private Context context;
    private boolean selected = false;
    private AnswersGridLayout.OnAnswerSelectedListener onAnswerListener;

    public MultipleAnswersView(@NonNull Context context) {
        super(context);
        init();
    }

    public MultipleAnswersView(Context context, Answer answer, AnswersGridLayout.OnAnswerSelectedListener onAnswerListener) {
        super(context);
        this.context = context;
        this.answer = answer;
        this.onAnswerListener = onAnswerListener;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.multiple_answers_button, this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setVisibility(INVISIBLE);
        setAlpha(0);
        setClickable(false);
        ((TextView) findViewById(R.id.multiple_answer_button_text)).setText(answer.getText());

        setOnClickListener(view -> {
            imageRes = selected ? R.drawable.ic_plus : R.drawable.ic_vi;
            textColor = selected ? R.color.answer_font_color : R.color.white;
            backgroundButton = selected ? R.drawable.multiple_button_answer_bg : R.drawable.multiple_button_answer_bg_selected;

            ((ImageView) (view.findViewById(R.id.multiple_answer_button_icon))).setImageResource(imageRes);
            ((TextView) view.findViewById(R.id.multiple_answer_button_text)).setTextColor(getResources().getColor(textColor));
            view.findViewById(R.id.multiple_answer_button_wrapper).setBackground(getResources().getDrawable(backgroundButton));
            selected = onAnswerListener.onAnswerSelected(answer);
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce));
        });
    }

    public void fadeIn(long delay, long duration) {
        setVisibility(VISIBLE);
        animate().alpha(1).setDuration(duration).setStartDelay(delay).withEndAction(() -> setClickable(true));
    }

    @Override
    public void answerShowAnimation(long delay) {
        fadeIn(delay, 250);
    }

}
