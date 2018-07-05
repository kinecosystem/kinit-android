package org.kinecosystem.kinit.view.customView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import org.kinecosystem.kinit.R;


public class AnswerSelectedOverView extends ConstraintLayout {


    public AnswerSelectedOverView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnswerSelectedOverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.answer_image_selected_view, this, true);
    }


}
