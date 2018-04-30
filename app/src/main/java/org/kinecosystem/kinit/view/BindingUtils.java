package org.kinecosystem.kinit.view;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import org.kinecosystem.kinit.util.ImageUtils;


public class BindingUtils {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            ImageUtils.loadImageIntoView(imageView.getContext(), url, imageView);
        }
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
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
}
