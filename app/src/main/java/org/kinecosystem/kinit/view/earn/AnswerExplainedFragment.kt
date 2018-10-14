package org.kinecosystem.kinit.view.earn

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.AnswerExplainedFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.AnswerViewModel


class AnswerExplainedFragment : BaseFragment() {
    private var answerModel: AnswerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = DataBindingUtil.inflate<AnswerExplainedFragmentBinding>(inflater, R.layout.answer_explained_fragment, container, false)
        if (activity !is QuestionnaireActions) {
            onInvalidData()
        }
        if (arguments == null || !arguments!!.containsKey(ARG_QUIZ_INDEX)) {
            onInvalidData()
        }
        val questionIndex = arguments!!.getInt(ARG_QUIZ_INDEX, INVALID_QUIZ_INDEX)
        if (questionIndex == INVALID_QUIZ_INDEX) {
            onInvalidData()
        }
        val questionnaireActions = activity as QuestionnaireActions?
        answerModel = AnswerViewModel(questionIndex, questionnaireActions!!)
        binding.model = answerModel
        return binding.root
    }

    private fun onInvalidData() {
        Log.e(TAG, "Invalid data cant start QuestionFragment")
        activity?.finish()
    }

    companion object {
        const val ARG_TASK_ID = "AnswerExplainedFragment_ARG_TASK_ID"
        const val INVALID_TASK_ID = ""
        const val ARG_QUIZ_INDEX = "QuizFragment_ARG_QUIZ_INDEX"
        const val INVALID_QUIZ_INDEX = -1
        val TAG:String = AnswerExplainedFragment::class.java.simpleName

        fun newInstance(taskId:String, questionIndex: Int): AnswerExplainedFragment {
            val fragment = AnswerExplainedFragment()
            val args = Bundle()
            args.putInt(ARG_QUIZ_INDEX, questionIndex)
            args.putString(ARG_TASK_ID, taskId)
            fragment.arguments = args
            return fragment
        }
    }
}
