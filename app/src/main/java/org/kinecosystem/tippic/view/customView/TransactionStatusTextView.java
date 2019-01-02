package org.kinecosystem.tippic.view.customView;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import org.kinecosystem.tippic.R;

public class TransactionStatusTextView extends android.support.v7.widget.AppCompatTextView {

    @BindingAdapter("android:text")
    public static void updateTransactionStatus(TransactionStatusTextView view, boolean transactionComplete) {
        int res = transactionComplete ? R.string.added_to_your_account : R.string.reward_comming_your_way;
        view.setText(res);
    }

    public TransactionStatusTextView(Context context) {
        super(context);
    }

    public TransactionStatusTextView(Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
