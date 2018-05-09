package org.kinecosystem.kinit.view.customView;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.view.customView.AnswersGridLayout.OnAnswerSelectedListener;

public class TextAnswerView extends android.support.v7.widget.AppCompatButton implements AnswersGridLayout.AnswerView  {

    private Answer answer;
    private boolean alignLeft;
    private OnAnswerSelectedListener listener;

    public TextAnswerView(Context context, Answer answer, AnswersGridLayout.OnAnswerSelectedListener listener, boolean alignLeft) {
        super(new ContextThemeWrapper(context, R.style.Button_Answer), null, R.style.Button_Answer);
        this.answer = answer;
        this.listener = listener;
        this.alignLeft = alignLeft;
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int gap = (int) getResources().getDimension(R.dimen.answer_gap);
        layoutParams.setMargins(0, 0, 0, gap);
        setLayoutParams(layoutParams);
        setVisibility(INVISIBLE);
        setAlpha(0);
        setClickable(false);
        setText(answer.getText());
        if (alignLeft) {
            setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            int paddingGap = (int) getResources().getDimension((R.dimen.emojy_gap));
            setPadding(paddingGap, 0, 0, 0);
        }
        setOnClickListener(view -> {
            if (answer != null && !listener.onAnswerSelected(answer)) {
                view.setBackground(getResources().getDrawable(R.drawable.answer_bg_selected));
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            }

        });
    }

    public void fadeIn(long delay, long duration) {
        setVisibility(VISIBLE);
        animate().alpha(1).setDuration(duration).setStartDelay(delay).withEndAction(() -> setClickable(true));
    }

    public void answerShowAnimation(long delay) {
        fadeIn(delay, 250);
    }


}
