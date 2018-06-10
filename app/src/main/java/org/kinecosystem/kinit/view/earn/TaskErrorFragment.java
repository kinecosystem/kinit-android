package org.kinecosystem.kinit.view.earn;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickCloseButtonOnErrorPage;
import org.kinecosystem.kinit.analytics.Events.Analytics.ViewErrorPage;
import org.kinecosystem.kinit.analytics.Events.Event;
import org.kinecosystem.kinit.repository.QuestionnaireRepository;
import org.kinecosystem.kinit.view.BaseActivity;
import org.kinecosystem.kinit.view.BaseFragment;


public class TaskErrorFragment extends BaseFragment {

    public static final String ARG_ERROR_TYPE = "Earn_error_type";
    public static final int ERROR_SUBMIT = 10;
    public static final int ERROR_TRANSACTION = 20;

    public static final String TAG = TaskErrorFragment.class.getSimpleName();
    @Inject
    Analytics analytics;
    @Inject
    QuestionnaireRepository questionnaireRepository;

    public static TaskErrorFragment newInstance(int error) {
        TaskErrorFragment fragment = new TaskErrorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ERROR_TYPE, error);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        KinitApplication.coreComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        int errorType = getArguments().getInt(ARG_ERROR_TYPE, ERROR_TRANSACTION);
        reportViewEvent(errorType);
        View view = inflater.inflate(getLayout(errorType), container, false);
        view.findViewById(R.id.close).setOnClickListener(view1 ->
            {
                reportClickOnCloseEvent(errorType);
                BaseActivity activity = (BaseActivity) getActivity();
                questionnaireRepository.resetTaskState();
                activity.finish();
            }
        );
        return view;
    }

    private int getLayout(int errorType) {
        if (getArguments() != null && getArguments().containsKey(ARG_ERROR_TYPE)) {
            if (errorType == ERROR_SUBMIT) {
                return R.layout.earn_error_submit_layout;
            }
            if (errorType == ERROR_TRANSACTION) {
                return R.layout.earn_error_transaction_layout;
            }
        }
        return R.layout.earn_error_submit_layout;
    }

    private void reportViewEvent(int errorType) {
        Event event;
        if (errorType == ERROR_TRANSACTION) {
            event = new ViewErrorPage(Analytics.VIEW_ERROR_TYPE_REWARD);
        } else { // ERROR_SUBMIT
            event = new ViewErrorPage(Analytics.VIEW_ERROR_TYPE_TASK_SUBMISSION);
        }
        analytics.logEvent(event);
    }

    private void reportClickOnCloseEvent(int errorType) {
        Event event;
        if (errorType == ERROR_TRANSACTION) {
            event = new ClickCloseButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_REWARD);
        } else { // ERROR_SUBMIT
            event = new ClickCloseButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_TASK_SUBMISSION);
        }
        analytics.logEvent(event);
    }
}
