package org.kinecosystem.kinit.view.earn;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.QuestionFragmentBinding;
import org.kinecosystem.kinit.view.BaseFragment;
import org.kinecosystem.kinit.view.adapter.AnswersListAdapter;
import org.kinecosystem.kinit.viewmodel.earn.QuestionViewModel;


public class QuestionFragment extends BaseFragment {

    public static final String ARG_QUESTION_INDEX = "QuestionnaireFragment_ARG_QUESTION_INDEX";
    public static final int INVALID_QUESTION_INDEX = -1;
    public static final String TAG = QuestionFragment.class.getSimpleName();
    private QuestionViewModel questionModel;

    public static QuestionFragment newInstance(int questionIndex) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        QuestionFragmentBinding binding = DataBindingUtil
            .inflate(inflater, R.layout.question_fragment, container, false);
        if (!(getActivity() instanceof QuestionnaireActions)) {
            onInvalidData();
        }
        if (getArguments() == null || !getArguments().containsKey(ARG_QUESTION_INDEX)) {
            onInvalidData();
        }
        final int questionIndex = getArguments().getInt(ARG_QUESTION_INDEX, INVALID_QUESTION_INDEX);
        if (questionIndex == INVALID_QUESTION_INDEX) {
            onInvalidData();
        }
        QuestionnaireActions questionnaireActions = (QuestionnaireActions) getActivity();
        questionModel = new QuestionViewModel(questionIndex, questionnaireActions);
        binding.answersRecycleView.setAdapter(new AnswersListAdapter(questionModel));
        binding.answersRecycleView.setLayoutManager(
            new GridLayoutManager(getContext(), questionModel.getColumnCount(), RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) binding.answersRecycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.answersRecycleView.setHasFixedSize(true);
        binding.setModel(questionModel);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        questionModel.onResume();
    }

    private void onInvalidData() {
        Log.e(TAG, "Invalid data cant start QuestionFragment");
        getActivity().finish();
    }
}
