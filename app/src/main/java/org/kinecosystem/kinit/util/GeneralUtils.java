package org.kinecosystem.kinit.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class GeneralUtils {

    public static void closeKeyboard(Context context, View view) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
