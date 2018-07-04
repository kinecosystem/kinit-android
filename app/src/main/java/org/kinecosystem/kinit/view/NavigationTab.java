package org.kinecosystem.kinit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.kinecosystem.kinit.R;

public class NavigationTab extends ConstraintLayout {

    private TextView title;
    private ImageView icon;
    private View notification;
    private int iconColor = getContext().getResources().getColor(R.color.tab_icon_color);
    private int iconSelectedColor = getContext().getResources().getColor(R.color.tab_icon_color_selected);

    public NavigationTab(@NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavigationTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int titleResId, imageResId;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.NavigationTab,
            0, 0);

        try {
            titleResId = a.getResourceId(R.styleable.NavigationTab_text, 0);
            imageResId = a.getResourceId(R.styleable.NavigationTab_src, 0);


        } finally {
            a.recycle();
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tab_navigation_item, this, true);
        title = view.findViewById(R.id.title);
        icon = view.findViewById(R.id.icon);
        notification = view.findViewById(R.id.notification);
        title.setText(titleResId);
        icon.setImageResource(imageResId);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        title.setSelected(selected);
        icon.setSelected(selected);
    }

    public void setNotificationIndicator(boolean toggle) {
        notification.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    public String title() {
        return title.getText().toString();
    }
}
