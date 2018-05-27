package org.kinecosystem.kinit.view.customView;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.model.earn.Answer;
import org.kinecosystem.kinit.util.ImageUtils;
import org.kinecosystem.kinit.view.customView.AnswersGridLayout.OnAnswerSelectedListener;

public class ImageTextAnswerView extends CardView implements AnswersGridLayout.AnswerView {

    private Answer answer;
    private OnAnswerSelectedListener listener;

    public ImageTextAnswerView(Context context, Answer answer, OnAnswerSelectedListener listener) {
        super(new ContextThemeWrapper(context, R.style.Button_ImageAnswer), null, R.style.Button_ImageAnswer);
        this.answer = answer;
        this.listener = listener;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.image_text_answer_layout, this);

        float radius_dp_dimen = context.getResources().getDimension(R.dimen.image_answer_corner);
        float radius_px_dimen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius_dp_dimen, context.getResources().getDisplayMetrics());

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        setLayoutParams(params);
        setUseCompatPadding(true);
        setPreventCornerOverlap(true);
        setRadius(radius_px_dimen);
        setOnClickListener(view -> {
            if (!listener.onAnswerSelected(answer)) {
                view.findViewById(R.id.answer_text).setBackground(getResources().getDrawable(R.drawable.image_answer_bg_selected));
                ((TextView) view.findViewById(R.id.answer_text)).setTextColor(getResources().getColor(R.color.white));
            }
        });
        TextView answer_text = findViewById(R.id.answer_text);
        answer_text.setBackground(getResources().getDrawable(R.drawable.image_text_answer_bg));
        if (!TextUtils.isEmpty(answer.getText()))
            answer_text.setText(answer.getText());
        else
            answer_text.setVisibility(GONE);


        View view = findViewById(R.id.answer_image);
        Context ivContext = view.getContext();
        ImageUtils.loadImageIntoView(ivContext, answer.getImageUrl(), findViewById(R.id.answer_image));
    }

    private void animateClick() {
        Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        startAnimation(bounce);
    }


    @Override
    public void answerShowAnimation(long delay) {
    }
}
